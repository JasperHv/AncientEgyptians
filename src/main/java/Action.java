import java.lang.reflect.Array;
import java.util.*;

public class Action {
    private String commandName;
    private Item item;
    private Room room;

    public Action(String commandName, Item item, Room room) {
        this.commandName = commandName;
        this.item = item;
        this.room = room;
    }

    public static void doAction(String[] command, Player player) {
        Action result;
        String keyword = command[0];

        switch (keyword) {
            case "move":
                if (command[1].equals("to")) {
                    Room currentRoom = player.getCurrentRoom();
                    String wholeName;
                    if (command.length > 3) {
                        String firstWord = command[2];
                        String secondWord = command[3];
                        wholeName = firstWord + " " + secondWord;
                    } else {
                        wholeName = command[2];
                    }
                    Room nextRoom = currentRoom.getNextRoomFromName(wholeName);

                    if (nextRoom == null) {
                        System.out.println(Main.ANSI_BLUE + "\nThe room name is invalid. Please ask for help.\n" + Main.ANSI_RESET);
                        break;
                    } else {
                        player.setCurrentRoom(nextRoom);
                        System.out.println(Main.ANSI_BLUE + "\nYou have moved to " + nextRoom.getRoomName() + ".\n" + player.getCurrentRoom().getScript() + "\n" + Main.ANSI_RESET);
                        break;
                    }
                }
                else {
                    System.out.println(Main.ANSI_BLUE + "\nDid you mean 'move to'?\n" + Main.ANSI_RESET);
                    break;
                }
            case "pick":
                if (command[1].equals("up")) {
                    HashMap<String, Item> currentItemsInRoom = player.getCurrentRoom().getItems();
                    ArrayList<Item> currentInventory = player.getInventory();

                    if (command.length < 3) {
                        System.out.println(Main.ANSI_BLUE + "\nYou cannot pick up nothing.\n" + Main.ANSI_RESET);
                        break;
                    }

                    String itemName = command[2];

                } else {
                    System.out.println(Main.ANSI_BLUE + "\nDid you mean 'pick up'?\n" + Main.ANSI_RESET);
                    break;
                }
            case "help":
                String[] roomOptions = player.getCurrentRoom().getNextRooms();
                String roomOptionsString = roomOptions[0];
                for (int i = 1; i < roomOptions.length; i++){
                    roomOptionsString = roomOptions[i] + ", " + roomOptionsString;
                }
                System.out.println(Main.ANSI_BLUE + "\nYou can move to " + roomOptionsString + ". To move, type 'move to <location>.'\n");
                break;
            default:
                System.out.println(Main.ANSI_BLUE + "\nThe command name is invalid. Please ask for help.\n" + Main.ANSI_RESET);
        }
    }
}
