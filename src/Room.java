public class Room {

    String value, name, place, building;

    public Room(String value, String name){
        this.value = value;
        if (name.contains("Emden")) {
            this.place = "Emden";
            this.name = name.replace("Emden:", "");
        } else {
            this.place = "Leer";
            this.name = name.replace("Leer:", "");
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return value + ", " + name;
    }
}
