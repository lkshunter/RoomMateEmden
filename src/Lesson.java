import java.time.LocalTime;

public class Lesson {

    public LocalTime start, stop;
    public String name;

    public Lesson(LocalTime start, int rowspan, String name ) {
        this.start = start;
        this.name = name;
        clacStop(rowspan);
    }

    private void clacStop(int times){
        stop = start;
        for (int i = 0; i < times; i++) {
            stop = stop.plusMinutes(15);
        }
    }

    public String toString() {
        return "" + name + "\nStart: " + start.toString() + " Stop: " + stop.toString();
    }

    public boolean equals(Lesson l){
        return start == l.start && stop == l.stop && name == l.name;
    }

    public boolean isTimeUsed(LocalTime lt){
        return start == lt;
    }

}
