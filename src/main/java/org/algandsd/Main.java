package org.algandsd;

import java.io.IOException;

public class Main {
    static void main(String[] args) throws IOException {
        if (args.length != 0) {
            CmdParams params = parseArgs(args);
            if (params.error) {
                System.out.println("Ошибка: параметры не вписаны или вписаны с ошибками");
            } else if (params.window) {
                new MainForm();
            }
        } else {
            System.out.println("Ошибка: параметры не вписаны или вписаны с ошибками");
        }
    }

    public static class CmdParams {
        public boolean window;
        public boolean error;
    }

    public static CmdParams parseArgs(String[] args) {
        CmdParams params = new CmdParams();
//        if (args[0].equals("--window")) {
            params.window = true;
            return params;
//        }
//        params.error = true;
//        return params;
    }
}
