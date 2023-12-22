public class point {

    public final int x;
    public final int y;

    public point(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o){
        if(o == this) {
            return true;
        }

        if(!(o instanceof point)){
            return false;
        }
        point c = (point) o;

        return c.x == x && c.y == y;
    }
}
