package com.netsec.toolkit.tools;

import com.netsec.toolkit.base.BaseToolActivity;

public class CvssCalcActivity extends BaseToolActivity {
    @Override protected String getToolTitle() { return "CVSS Score Calculator"; }
    @Override protected String getCategoryColor() { return "#DD2C00"; }
    @Override protected String getExecuteLabel() { return "Calculate CVSS"; }
    @Override protected String[] getInputHints() { return new String[]{"CVSS v3.1 vector (e.g. AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H)"}; }

    @Override
    protected void performAction(String[] inputs, ResultCallback cb) {
        String vector = inputs[0].trim();
        if (vector.startsWith("CVSS:3")) vector = vector.substring(vector.indexOf("/")+1);
        try {
            java.util.Map<String,String> m = new java.util.HashMap<>();
            for (String part : vector.split("/")) {
                String[] kv = part.split(":", 2);
                if (kv.length == 2) m.put(kv[0], kv[1]);
            }
            double score = calcCvss31(m);
            String severity = score == 0.0 ? "NONE" : score < 4.0 ? "LOW" : score < 7.0 ? "MEDIUM" : score < 9.0 ? "HIGH" : "CRITICAL";
            StringBuilder sb = new StringBuilder("CVSS v3.1 Calculator\n\nVector: " + vector + "\n\n");
            sb.append("── Metrics ─────────────────────────\n");
            String[] labels = {"AV","AC","PR","UI","S","C","I","A"};
            String[] names  = {"Attack Vector","Attack Complexity","Privileges Required","User Interaction","Scope","Confidentiality","Integrity","Availability"};
            for (int i = 0; i < labels.length; i++) sb.append(String.format("%-25s : %s\n", names[i], m.getOrDefault(labels[i],"N/A")));
            sb.append("\n── Score ───────────────────────────\n");
            sb.append("Base Score : ").append(String.format("%.1f", score)).append(" / 10.0\n");
            sb.append("Severity   : ").append(severity).append("\n\n");
            sb.append("── Severity Scale ──────────────────\n");
            sb.append("0.0       : None\n0.1-3.9   : Low\n4.0-6.9   : Medium\n7.0-8.9   : High\n9.0-10.0  : Critical");
            cb.onResult(sb.toString());
        } catch (Exception e) { cb.onError("Invalid CVSS vector: " + e.getMessage()); }
    }

    private double calcCvss31(java.util.Map<String,String> m) {
        double AV = val(m.get("AV"), new String[]{"N","A","L","P"}, new double[]{0.85,0.62,0.55,0.2});
        double AC = val(m.get("AC"), new String[]{"L","H"}, new double[]{0.77,0.44});
        double PR = val(m.get("PR"), new String[]{"N","L","H"}, new double[]{0.85,0.62,0.27});
        double UI = val(m.get("UI"), new String[]{"N","R"}, new double[]{0.85,0.62});
        boolean changed = "C".equals(m.get("S"));
        if (changed && "L".equals(m.get("PR"))) PR = 0.68;
        if (changed && "H".equals(m.get("PR"))) PR = 0.50;
        double C = impVal(m.get("C")), I = impVal(m.get("I")), A = impVal(m.get("A"));
        double ISCBase = 1 - (1-C)*(1-I)*(1-A);
        double ISC = changed ? 7.52*(ISCBase-0.029) - 3.25*Math.pow(ISCBase-0.02, 15) : 6.42*ISCBase;
        if (ISC <= 0) return 0.0;
        double exploitability = 8.22*AV*AC*PR*UI;
        double score = changed ? Math.min((ISC+exploitability)*1.08, 10) : Math.min(ISC+exploitability, 10);
        return Math.round(score * 10.0) / 10.0;
    }

    private double val(String key, String[] keys, double[] vals) {
        if (key == null) return vals[0];
        for (int i = 0; i < keys.length; i++) if (keys[i].equals(key)) return vals[i];
        return vals[0];
    }

    private double impVal(String v) {
        if ("N".equals(v)) return 0.0; if ("L".equals(v)) return 0.22; return 0.56;
    }
}
