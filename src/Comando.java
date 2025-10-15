public class Comando {
    private final String tipo;
    private final int arg1;
    private final int arg2;

    public Comando(String tipo, int arg1, int arg2) {
        this.tipo = tipo;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getTipo() {
        return tipo;
    }

    public int getArg1() {
        return arg1;
    }

    public int getArg2() {
        return arg2;
    }

    @Override
    public String toString() {
        return String.format("Comando[%s, %d, %d]", tipo, arg1, arg2);
    }
}
