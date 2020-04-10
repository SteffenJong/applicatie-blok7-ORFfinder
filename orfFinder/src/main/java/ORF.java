public class ORF {

    private int startPosition;
    private int length;
    private int ORFNumber;
    private String direction; //reading frame
    private String sequence;

    public void setStartPosition(int position) { this.startPosition = position; }
    public void setLength(int sequenceLength) { this.length = sequenceLength; }
    public void setORFNumber(int counter) { this.ORFNumber = counter; }
    public void setDirection(String frame) { this.direction = frame; }
    public void setSequence(String seq) { this.sequence = seq; }

    public int getStartPosition() { return this.startPosition; }
    public int getLength() { return this.length; }
    public int getORFNumber() { return this.ORFNumber; }
    public String getDirection() { return this.direction; }
    public String getSequence() { return this.sequence; }
}