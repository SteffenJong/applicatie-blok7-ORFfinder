//import java.io.*;
//import java.nio.charset.StandardCharsets;
//
//public class blast {
//    Process mProcess;
//
//    public void runScript() {
//        Process process;
//        try {
//            process = Runtime.getRuntime().exec(new String[]{"src/main/python/test.py", "arg1", "arg2"});
//            mProcess = process;
//        InputStream stdout = mProcess.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
//        String line;
//
//            while ((line = reader.readLine()) != null) {
//                System.out.println("stdout: " + line);
//            }
//        } catch (IOException e) {
//            System.out.println("Exception in reading output" + e.toString());
//        }
//    }
//}

