import java.util.*;

public class ORFFinder {

    public String inputSequence;
    public String forwardInput;
    public String reverseInput;
    public String frame1plus;
    public String frame2plus;
    public String frame3plus;
    public String frame1minus;
    public String frame2minus;
    public String frame3minus;
    public String startCodon = "ATG";
    public List<String> stopCodons = Arrays.asList("TAA", "TGA", "TAG");
    public int ORFCounter = 0;
    //RETURNEERD HASMAP MET ID, ORF OBJECT (key = id ; objectreferentie)
    public HashMap<Integer, ORF> foundORFs = new HashMap<Integer, ORF>();


    ORFFinder(String inputseq) {
        this.inputSequence = inputseq;
        makeOrfs();
    }

    public Map<Integer, ORF> getORfs() {
        return foundORFs;
    }

    private void makeOrfs() {
        this.forwardInput = this.inputSequence;
        this.frame1plus = this.forwardInput;
        this.frame2plus = this.frame1plus.substring(1);
        this.frame3plus = this.frame2plus.substring(1);
        StringBuilder reversed;
        StringBuilder stringBuild = new StringBuilder();
        stringBuild.append(this.inputSequence);
        reversed = stringBuild.reverse();
        this.reverseInput = reversed.toString();
        this.frame1minus = this.reverseInput;
        this.frame2minus = this.frame1minus.substring(1);
        this.frame3minus = this.frame2minus.substring(1);

        ORF[] ORFs = new ORF[1000];
        String forInput = "";
        for (int i1 = 0; i1 < 6; i1++) {
            if (i1 == 0) {
                forInput = this.frame1plus;
            } else if (i1 == 1) {
                forInput = this.frame2plus;
            } else if (i1 == 2) {
                forInput = this.frame3plus;
            } else if (i1 == 3) {
                forInput = this.frame1minus;
            } else if (i1 == 4) {
                forInput = this.frame2minus;
            } else if (i1 == 5) {
                forInput = this.frame3minus;
            }
            String temp = "";
            boolean done = false;
            boolean startFound = false;
            int startIndex = 0;
            List<String> tempDataList = new ArrayList<>();
            String buildingSequence = "";
            String buildingSequenceCopy = "";

            for (int i = 0; i < forInput.length(); ) {
                try {
                    temp = forInput.substring(i, i + 3);
                } catch (StringIndexOutOfBoundsException e) {
                    done = true;
                }
                if (temp.equals(this.startCodon)) {
                    startFound = true;
                    startIndex = i;
                } else if (temp.equals(this.stopCodons.get(0))) {
                    if (startFound) {
                        this.ORFCounter++;
                        ORFs[this.ORFCounter] = new ORF();
                        ORFs[this.ORFCounter].setStartPosition(startIndex+1);
                        if (i1 == 0) {
                            ORFs[this.ORFCounter].setDirection("frame +1");
                        } else if (i1 == 1) {
                            ORFs[this.ORFCounter].setDirection("frame +2");
                        } else if (i1 == 2) {
                            ORFs[this.ORFCounter].setDirection("frame +3");
                        } else if (i1 == 3) {
                            ORFs[this.ORFCounter].setDirection("frame -1");
                        } else if (i1 == 4) {
                            ORFs[this.ORFCounter].setDirection("frame -2");
                        } else if (i1 == 5) {
                            ORFs[this.ORFCounter].setDirection("frame -3");
                        }
                        ORFs[this.ORFCounter].setLength(i - startIndex);
                        ORFs[this.ORFCounter].setORFNumber(this.ORFCounter);
                        //The assembly of the ORF sequence   buildingSequence buildingSequenceCopy
                        int i3 = i / 3;
                        while (startIndex / 3 <= i3) {
                            if (i3 >= tempDataList.size()) {
                                ;
                            } else {
                                buildingSequenceCopy = tempDataList.get(i3) + buildingSequence;
                                buildingSequence = buildingSequenceCopy;
                            }
                            i3--;
                        }
                        tempDataList.clear();
                        ORFs[this.ORFCounter].setSequence(buildingSequence);
                        this.foundORFs.put(this.ORFCounter, ORFs[this.ORFCounter]);
                    }
                    startFound = false;
                } else if (temp.equals(this.stopCodons.get(1))) {
                    if (startFound) {
                        this.ORFCounter++;
                        System.out.println(this.ORFCounter);
                        ORFs[this.ORFCounter] = new ORF();
                        ORFs[this.ORFCounter].setStartPosition(startIndex+1);
                        if (i1 == 0) {
                            ORFs[this.ORFCounter].setDirection("frame +1");
                        } else if (i1 == 1) {
                            ORFs[this.ORFCounter].setDirection("frame +2");
                        } else if (i1 == 2) {
                            ORFs[this.ORFCounter].setDirection("frame +3");
                        } else if (i1 == 3) {
                            ORFs[this.ORFCounter].setDirection("frame -1");
                        } else if (i1 == 4) {
                            ORFs[this.ORFCounter].setDirection("frame -2");
                        } else if (i1 == 5) {
                            ORFs[this.ORFCounter].setDirection("frame -3");
                        }
                        ORFs[this.ORFCounter].setLength(i - startIndex);
                        ORFs[this.ORFCounter].setORFNumber(this.ORFCounter);
                        //The assembly of the ORF sequence   buildingSequence buildingSequenceCopy
                        int i3 = i / 3;
                        while (startIndex / 3 <= i3) {
                            if (i3 >= tempDataList.size()) {
                                ;
                            } else {
                                buildingSequenceCopy = buildingSequence + tempDataList.get(i3);
                                buildingSequence = buildingSequenceCopy;
                            }
                            i3--;
                        }
                        tempDataList.clear();
                        ORFs[this.ORFCounter].setSequence(buildingSequence);

                        this.foundORFs.put(this.ORFCounter, ORFs[this.ORFCounter]);
                    }
                    startFound = false;
                } else if (temp.equals(this.stopCodons.get(2))) {
                    if (startFound) {
                        this.ORFCounter++;
                        System.out.println(this.ORFCounter);
                        ORFs[this.ORFCounter] = new ORF();
                        ORFs[this.ORFCounter].setStartPosition(startIndex+1);
                        if (i1 == 0) {
                            ORFs[this.ORFCounter].setDirection("frame +1");
                        } else if (i1 == 1) {
                            ORFs[this.ORFCounter].setDirection("frame +2");
                        } else if (i1 == 2) {
                            ORFs[this.ORFCounter].setDirection("frame +3");
                        } else if (i1 == 3) {
                            ORFs[this.ORFCounter].setDirection("frame -1");
                        } else if (i1 == 4) {
                            ORFs[this.ORFCounter].setDirection("frame -2");
                        } else if (i1 == 5) {
                            ORFs[this.ORFCounter].setDirection("frame -3");
                        }
                        ORFs[this.ORFCounter].setLength(i - startIndex);
                        ORFs[this.ORFCounter].setORFNumber(this.ORFCounter);
                        //The assembly of the ORF sequence   buildingSequence buildingSequenceCopy
                        int i3 = i / 3;
                        while (startIndex / 3 <= i3) {
                            if (i3 >= tempDataList.size()) {
                                ;
                            } else {
                                buildingSequenceCopy = buildingSequence + tempDataList.get(i3);
                                buildingSequence = buildingSequenceCopy;
                            }
                            i3--;
                        }
                        tempDataList.clear();
                        ORFs[this.ORFCounter].setSequence(buildingSequence);
                        this.foundORFs.put(this.ORFCounter, ORFs[this.ORFCounter]);
                    }
                    startFound = false;
                }
                if (!done) {
                    tempDataList.add(temp);
                }
                i += 3;
            }

        }
    }
}


