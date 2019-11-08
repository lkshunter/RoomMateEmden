import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Timetabel {

    String url;
    Document doc;
    ArrayList<Room> rooms = new ArrayList<>();

    public Timetabel(String url) {
        this.url = url;
        doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception ex) {
            System.out.println("Doof");
        }
    }

    public String getValueForRoom(String roomPara){
        String re = "null";
        for (Room ro : rooms) {
            if (ro.name.contains(roomPara)) {
               re = ro.value;
            }
        }
        return re;
    }

    public ArrayList<Lesson> getAllLessonsAtDay(){
        ArrayList<Lesson> lessons = new ArrayList<>();

        LocalTime time = LocalTime.MIN;
        for (Element table : doc.select("table.timetablepane tr")) {
            for (Element row : table.select("tr")) {
                String name = "";
                for (Element th : row.select("th")) {
                    if (th.text().contains(":")) {
                        String[] numbers = th.text().split(":");
                        LocalTime timeTemp = LocalTime.MIN;
                        timeTemp = timeTemp.plusHours(Integer.parseInt(numbers[0])).plusMinutes(Integer.parseInt(numbers[1]));
                        if (timeTemp.isAfter(time)) {
                            time = timeTemp;

                            for (Element td : row.select("td[rowspan]")) {
                                Integer i = Integer.parseInt(td.attr("rowspan"));
                                if (i > 1) {
                                    Lesson unit = new Lesson(time, Integer.parseInt(td.attr("rowspan")), td.text());

                                    if (!isTimeInArray(lessons, time)) {
                                        lessons.add(unit);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return lessons;
    }

    public boolean isTimeInArray(ArrayList<Lesson> l, LocalTime t){
        boolean check = false;

        for (Lesson  les: l) {
            check = les.isTimeUsed(t);
        }

        return check;
    }

    public void fillRoomTable(){
        Elements options = doc.getElementsByAttributeValue("name", "id").get(0).children();
        for (Element option : options) {
            if (option.hasAttr("value")) {
                String s1 = option.val();
                String s2 = option.text().replace(" ", "");
                rooms.add(new Room(option.val().toString(), option.text().replace(" ", "")));
            }
        }
    }

    private void printAllHTML(Document doc){
        System.out.println(doc.outerHtml());
    }

}
