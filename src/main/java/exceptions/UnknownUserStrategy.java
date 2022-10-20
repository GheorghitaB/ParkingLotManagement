package exceptions;

public class UnknownUserStrategy extends IllegalStateException{
    public UnknownUserStrategy(String msg){super(msg);}
}
