import java.time.LocalTime;

public class Lesson {

    LocalTime start, stop;
    String type, subject, teacher;

    public Lesson(LocalTime start, int rowspan, String name ) {
        clacStop(rowspan);
    }

    private void clacStop(int times){
        stop = start;
        for (int i = 0; i < times; i++) {
            stop.plusMinutes(15);
        }
    }
}
