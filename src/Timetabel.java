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

        for (Element table : doc.select("table.timetablepane tr")) {
            for (Element row : table.select("tr")) {
                LocalTime time = LocalTime.MIN;
                String name = "";
                for (Element th : table.select("th")) {
                    if (th.text().length() == 5) {
                        String[] numbers = th.text().split(":");
                        time.plusHours(Integer.parseInt(numbers[0])).plusMinutes(Integer.parseInt(numbers[1]));
                    }
                }
                for (Element td : table.select("td")) {
                    if (td.hasText()) {
                        name = td.html().replace("<br>", "\n").replace("<b>","").replace("</b>","");
                        lessons.add(new Lesson(time, Integer.parseInt(td.val("rowspan").toString()),td.text()));
                    }
                }
            }
        }
        return lessons;
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
