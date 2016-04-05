/**
 *  This class is the main class of the "Adventure Park" application. 
 *  "Adventure Park" is an exciting, text based adventure game.  Users 
 *  can walk around a map and interact with various item objects.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Isaiah Wesley
 * @version April 04, 2016
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room prevRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        
        Room CaptCove, AdvLand, AquaWorld, KidLand, Bathroom, FlamingJoes, HappyTrails, AbandonedCastle, Basement, Attic, SecFloor, GiftShop;
     
        // create the rooms
        CaptCove = new Room("in the Captain Cove's section of the theme park");
        AdvLand = new Room("in the main entrance of the theme park");
        AquaWorld = new Room("in the water park section of the theme park.");
        KidLand = new Room("in the children's section of the theme park.");
        Bathroom = new Room("in the park's bathroom");
        FlamingJoes = new Room("in a barbecue restaurant");
        HappyTrails = new Room("in the woods");
        AbandonedCastle = new Room("in an eerie, decrepit castle");
        Basement = new Room("in the castle's basement");
        Attic = new Room("in the castle's attic");
        SecFloor = new Room("on the second floor of FlamingJoes");
        GiftShop = new Room("on the bottom floot of FlamingJoes"); 
                 
        //initialise room exits
        CaptCove.setExit("east", AquaWorld);
        CaptCove.setExit("south", Bathroom);
        CaptCove.setExit("west", AdvLand);
        
        AdvLand.setExit("north", HappyTrails);
        AdvLand.setExit("east", CaptCove);
        AdvLand.setExit("west", FlamingJoes);
        
        AquaWorld.setExit("west", CaptCove);
        
        KidLand.setExit("north", HappyTrails);
        
        Bathroom.setExit("north", CaptCove);
        
        FlamingJoes.setExit("up", SecFloor);
        FlamingJoes.setExit("down", GiftShop);
        FlamingJoes.setExit("east", AdvLand);
        
        HappyTrails.setExit("north", AbandonedCastle);
        HappyTrails.setExit("south", AdvLand);
        
        AbandonedCastle.setExit("south", HappyTrails);
        AbandonedCastle.setExit("up", Attic);
        AbandonedCastle.setExit("down", Basement);
        
        Basement.setExit("up", AbandonedCastle);
        
        Attic.setExit("down", AbandonedCastle);
        
        SecFloor.setExit("down", FlamingJoes);
        
        GiftShop.setExit("up", FlamingJoes);
        
                
        currentRoom = AdvLand;  // start game in Adventure Land
        
        //initialise game items
        Item EyePatch = new Item("the captain's legendary eyepatch", 100);
        Item RustySword = new Item("the sharp sword", 12000);
        Item RubberDucky = new Item("A child's favorite toy --Rubber Ducky", 40);
        Item HawaiiShirt = new Item("a snazzy, well-designed article of clothing --Hawaiian Shirt", 234);
        
        //places the items in a specified location
        CaptCove.placeItem(EyePatch);
        AbandonedCastle.placeItem(RustySword);
        AquaWorld.placeItem(RubberDucky);
        GiftShop.placeItem(HawaiiShirt);

    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing Adventure Park.  Until next time!");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Adventure Park, where a journey of a thousand miles begins with the first step!");
        System.out.println("Adventure Park is a new, incredibly exciting text adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;
                
            case BACK:
                goBack();
                break;
                
            case LOOK:
                look();
                break;
                
            case EAT:
                eat();
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the amusement park.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
   
    /**
     * Prints out some information about
     * your current location.
     */
    private void DirectionInfo() {
        System.out.println("Your current location: " + currentRoom.getLongDescription());
        
    }
    
    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    /**
     * looks to see which room the user is currently in
     * and prints out a description.
     */private void look() {
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Prints out a message informing the user that 
     * the character has ate some nutrituous food.
     */private void eat() {
        System.out.println("You have replenished your hunger to its maximum capacity! You are not hungry anymore.");
    }
   /**
    * Checks to see if a previous room is available to go back into.
    * If the program can not find such a room, the user has to find another way out.
    * Otherwise, the user will be able to go back to the previous room.
    */ private void goBack() {
        if (prevRoom == null) {
            System.out.println("it appears that there are no rooms to go back to. Find another way out!");
        }
        else {
            currentRoom = prevRoom;
            System.out.println("You have gone back to: " + "\n" + currentRoom.getLongDescription());
    }
}

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Allows the user to run the game in the main method 
     * rather than through BlueJ.
     */public static void main(String[] args){
        Game game = new Game();
        game.play();
    }
}
