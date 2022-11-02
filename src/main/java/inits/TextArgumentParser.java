package inits;

import java.io.*;

public abstract class TextArgumentParser {
    private static final String COMMENT_SYMBOL = "#";
    protected static final String lineSplitByString = " ";
    protected static final int UNSUCCESSFUL_TERMINATION_WITH_EXCEPTION = -1;

    protected static boolean isComment(String line){
        return line.startsWith(COMMENT_SYMBOL);
    }

    protected static boolean skipLine(String line){
        return isComment(line) || line.isEmpty();
    }

    protected static String prepareLine(String line) {
        return line.strip().toUpperCase();
    }

    protected static String[] getArgumentsFromLine(String line) {
        return line.split(lineSplitByString);
    }

    protected static void closeStreams(InputStream is, InputStreamReader isr, BufferedReader br){
        if(br != null){
            try { br.close();}
            catch (IOException e) {
                System.out.println("Buffered Reader cannot be closed");
            }
        }
        if(isr != null){
            try{isr.close();}
            catch (IOException e){System.out.println("Input Stream Reader cannot be closed");}
        }
        if(is != null){
            try{is.close();}
            catch (IOException e){System.out.println("Input Stream cannot be closed");}
        }
    }
}
