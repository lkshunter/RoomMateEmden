import com.pengrad.telegrambot.model.Update;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Command {

    String url;
    String urlClean;

    public Command(String url, String urlClean){
        this.url = url;
        this.urlClean = urlClean;
    }

    public String action(Update update){
        return useCommands(update);
    }

    private String useCommands(Update update) {
        String command = refineCommand(update.message().text());
        System.out.println(command + ": from " + update.message().from().firstName() + " " + update.message().from().id() + " at " + LocalTime.now());
        switch (command) {
            case "start":
                return commandStart(update);
            case "now":
                return commandNow(update, urlClean);
            case "today":
                return commandToday(update);
            case "day":
                return commandDay(update);
            case "help":
                return commandHelp(update.message().text());
            default:
                return "Uuups! Something went wrong.\n \"/" + command + "\" is no valid command.\nPlease look if it's right spelled.";
        }
    }

    // Commands
    private String commandDay(Update update) {
        String buildetURL = buildURL(update, 0);
        Timetabel test = new Timetabel(buildURL(update, 0));
        ArrayList<Lesson> n = test.getAllLessonsAtDay();

        return "";
    }

    private String commandToday(Update update) {
        String buildetURL = buildURL(update, 0);
        Timetabel test = new Timetabel(buildURL(update, 0));
        ArrayList<Lesson> n = test.getAllLessonsAtDay();

        LocalTime now = LocalTime.now();

        String result = "";

        if (!n.isEmpty()) {
            for (Lesson l : n) {
                result += l.toString() + "\n\n";
            }
        } else {
            result = "At this day exists no Task in this room.";
        }

        return result;
    }

    private String commandNow(Update update, String clean) {
        String buildetURL = buildURL(update, 0);
        Timetabel test = new Timetabel(buildURL(update, 0));
        ArrayList<Lesson> n = test.getAllLessonsAtDay();

        LocalTime now = LocalTime.now();

        String result = "\u2714 - This room is currently free.";

        if (!n.isEmpty()) {
            for (Lesson l : n) {
                if (l.start.isBefore(now) && l.stop.isAfter(now)) {
                    result = "\u274C - This room is currently occupied by \"" + l.name + "\" from " + l.start + " till " + l.stop;
                }
            }
        }
        return result;
    }

    private String commandStart(Update update) {

        String welcome = "Moin " + update.message().from().username() + "\nThis bot should help you to check if rooms are in use or free. For example you can type: \"/now E206\" to check if this room is now free.";

        return welcome;
    }

    private String commandHelp(String help) {
        String para = refineParameter(help);

        switch (para) {
            case "start":
                return "This command sends you a welcome message.";
            case "now":
                return "This command show you if a specific room is now in use.";
            case "today":
                return "This command show you a table of all times when this room is used today.";
            case "null":
                return "Please add a parameter like \"start\". You can add every command to get more information";
        }
        return help;
    }

    // Helper methods
    private String refineCommand(String input) {
        String command = "";
        if (input.contains("/")) {
            for (char c : input.toCharArray()) {
                if (c != '/' && c != ' ') {
                    command += c;
                }
                if (c == ' ') {
                    break;
                }
            }
        } else {
            command = "null";
        }
        return command;
    }

    private String refineParameter(String input) {
        String[] parameter = input.split(" ");
        String re;
        if (parameter.length > 1) {
            re = parameter[1];
        } else {
            re = "null";
        }
        return re;
    }

    private String[] refineAllParameter(String input) {
        String[] re = input.split(" ");
        if (re.length == 1) {
            return null;
        }
        return re;
    }

    public String buildURL(Update update, int nextWeek) {
        String special = urlClean;
        Timetabel table = new Timetabel(url);
        table.fillRoomTable();
        Date dat = new Date();
        Calendar cal = Calendar.getInstance(Locale.GERMANY);
        cal.setTime(dat);

        String id = "&id=";
        id += table.getValueForRoom(refineParameter(update.message().text()));

        special += id;
        special = special.replace("WEEK", "" + (cal.get(cal.WEEK_OF_YEAR) + nextWeek));
        special = setDays(cal.get(cal.DAY_OF_WEEK), special);
        return special;
    }

    public String buildURL(Update update, String day, int nextWeek) {
        String special = urlClean;
        Timetabel table = new Timetabel(url);
        table.fillRoomTable();

        int weekNr = (int) ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR.getFrom((TemporalAccessor) LocalDate.now());

        special += "&id=" + table.getValueForRoom(refineParameter(update.message().text()));
        special = special.replace("WEEK", "" + (int) ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR.getFrom((TemporalAccessor) LocalDate.now()));
        //special = setDays(, special);
        return special;
    }

    private String setDays(int day, String s){
        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

        for (int i = 0; i < days.length; i++) {
            if(i != day-2) {
                s = s.replace(days[i], "0");
            } else if(i == day-2) {
                s = s.replace(days[i], "1");
            }
        }
        return s;
    }

    private String setDays(String day, String s){
        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

        for (int i = 0; i < days.length; i++) {
            if(day.toUpperCase().equals(days[i])) {
                s = s.replace(days[i], "0");
            } else {
                s = s.replace(days[i], "1");
            }
        }
        return s;
    }

}
