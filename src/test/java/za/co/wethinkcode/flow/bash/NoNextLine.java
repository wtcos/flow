package za.co.wethinkcode.flow.bash;

public class NoNextLine extends RuntimeException {

    public NoNextLine() {
        super("The BashInterpreter has tried to read past the end of the output.");
    }
}
