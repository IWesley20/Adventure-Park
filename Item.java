/**
 * This class is part of the "Adventure Park" application.
 * Sets the description and weight of a particular item. 
 * 
 * @author  Isaiah Wesley
 * @version April 04, 2016
 */
public class Item {
    private String description;
    private int weight;
    
    public Item(String description, int weight){
        this.description = description;
        this.weight = weight;
}
    public String getDescription() {
        return description;
} 

    public int getWeight() {
        return weight;
}
}