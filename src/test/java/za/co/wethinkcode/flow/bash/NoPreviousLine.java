package za.co.wethinkcode.flow.bash;

public class NoPreviousLine extends RuntimeException {
    public NoPreviousLine() {
        super("The BashInterpreter has tried to go backwards past the beginning of the file.");
    }
}
