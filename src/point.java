public class point {

    public final int x;
    public final int y;

    public point(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o){

        if(!(o instanceof point)){
            return false;
        }
        point c = (point) o;

        return c.x == this.x && c.y == this.y;
    }

    @Override
    public int hashCode() {
        return (this.x << 2) ^ this.y;
        // x shifted left by two, and XOR'd with y. Taken from an example hashcode from Microsoft
        // https://learn.microsoft.com/en-us/dotnet/api/system.object.equals?view=net-8.0
    }
}
