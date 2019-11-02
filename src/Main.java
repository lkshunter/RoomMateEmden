import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Main {

    public static Command command;

    public static void main(String[] args) {

        TelegramBot bot = new TelegramBot("");

        String url = "http://splan.hs-el.de/index.php?modus=2&fb=0&kw=43&kwstart=43&print=0&infos=0&mo=1&di=1&mi=1&do=1&fr=1&sa=1&so=1&showkw=0";
        String urlClean = "http://splan.hs-el.de/index.php?modus=2&fb=0&kw=WEEK&kwstart=43&print=1&infos=0&mo=MONDAY&di=TUESDAY&mi=WEDNESDAY&do=THURSDAY&fr=FRIDAY&sa=SATURDAY&so=SUNDAY&showkw=0";



        command = new Command(url, urlClean);
        start(bot);

    }

    public static void start(TelegramBot tBot) {
        int m = 0;

        while (true) {
            List<Update> updates = tBot.execute(new GetUpdates().limit(100).offset(m)).updates();

            for (Update update : updates) {
                m = update.updateId() + 1;
                String sendMessage = "";
                if (update.message() != null) {
                    tBot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                    sendMessage = command.action(update);

                    tBot.execute(new SendMessage(update.message().chat().id(), sendMessage.toString()));
                }
            }
        }
    }


}
