package stablemarriages;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Represents a girl in the stable marriage problem.
 */

public class Girl implements Runnable {
    private Integer             id;
    private List<Integer>       preferences;
    private ProposalInbox       inbox;
    private AppendOnlyMailbox[] mailboxesForBoys;
    private StateOfDay          state;
    private Integer             currentSuitor = null;
    private TreeSet<Integer>    rankedProposals = new TreeSet<>();

    // Representation Invariant:
    // - this.mailboxesForBoys.length == preferences.size()
    // - this.inbox is not null

    /**
     * Create an instance of a girl
     * @param id unique identifier for this girl
     * @param preferences is not null and contains a permutation of [0, 1, ..., preferences.size() - 1]
     * @param inbox is not null
     * @param mailboxesForBoys does not contain nulls and mailboxesForBoys.length == preferences.size()
     * @param state shared state of the day, is not null
     */
    public Girl(Integer id, List<Integer> preferences, ProposalInbox inbox, AppendOnlyMailbox[] mailboxesForBoys, StateOfDay state) {
        this.id               = id;
        this.preferences      = new ArrayList<>(preferences);
        this.inbox            = inbox;
        this.mailboxesForBoys = mailboxesForBoys;
        this.state            = state;
    }

    /**
     * Obtain the id of this girl
     * @return the id of this girl
     */
    public int getID() {
        return id;
    }

    /**
     * Obtain the current suitor
     *
     * @return the current suitor
     * (could be null if the girl has not received a proposal yet)
     */
    public Integer getCurrentSuitor() {
        return currentSuitor;
    }

    /**
     * The traditional marriage algorithm
     */

    //synchronized only needs to be used when fetching mutable data so the use of synchronized does not seem to be excessive
    public void run() {
        int day = 0;

        while (!state.isMatchingDone()) {
            day = state.getDay();

            while (!state.isMorningDone()) { }

                if(!inbox.getProposals().isEmpty()){
                    Integer tempSuiter = -1;
                    Integer endSuiter = inbox.getProposals().get(0);
                    for(Integer proposal: inbox.getProposals()){
                        tempSuiter = proposal;
                        if(preferences.indexOf(tempSuiter) < preferences.indexOf(endSuiter)){
                            mailboxesForBoys[endSuiter].post(-1);
                            endSuiter = tempSuiter;

                        }else if(tempSuiter==endSuiter) {
                            continue;
                        }else{
                            mailboxesForBoys[tempSuiter].post(-1);

                        }
                    }

                    if(preferences.indexOf(endSuiter)< preferences.indexOf(currentSuitor)|| currentSuitor==null){
                        if(currentSuitor!=null) {
                            mailboxesForBoys[currentSuitor].post(-1);
                        }
                        currentSuitor = endSuiter;
                        mailboxesForBoys[endSuiter].post(endSuiter);
                    } else if(currentSuitor ==endSuiter){
                        mailboxesForBoys[currentSuitor].post(currentSuitor);
                    } else{
                        mailboxesForBoys[endSuiter].post(-1);
                    }

                }

                inbox.getProposals().clear();
                state.setAfternoonDone(id);
            while (day == state.getDay()){}


        }
    }
}
