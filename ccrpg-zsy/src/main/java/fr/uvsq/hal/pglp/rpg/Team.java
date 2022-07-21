package fr.uvsq.hal.pglp.rpg;

import java.util.*;

/**
 * La classe <code>Team</code> représente un groupe de personnages.
 *
 * @author hal
 * @version 2022
 */
public class Team implements OrganizationElement, Iterable<OrganizationElement>{
    private List<OrganizationElement> members;

    public Team() {
        members = new ArrayList<>();
    }

    //ne pas permettre la création de groupes se contenant eux-même même indirectement.
    public void add(OrganizationElement element) {
        if(this != element){
            members.add(element);
        }
    }

    //Définissez la méthode getSize qui déterminera (Recursive) le nombre de personnages dans un groupe.
    public int getSize() {
        int n = members.size();
        for(OrganizationElement membersElement: members){
            if(membersElement instanceof Team){
                n = n + ((Team)membersElement).getSize() - 1 ;
            }
        }
        return n;
    }

    //contains() déterminera si un Team contient un character, Fait Une Recherche Recursive
    public boolean contains(OrganizationElement element) {
        boolean b = members.contains(element);
        for(OrganizationElement membersElement: members){
            if(membersElement instanceof Team){
                b = b || ((Team)membersElement).contains(element);
            }
        }
        return b;
    }

    @Override
    public Iterator<OrganizationElement> iterator() {
        return new TeamIterator(members);
    }

    private static class TeamIterator implements Iterator<OrganizationElement> {
        private Stack<Iterator<OrganizationElement>> iteratorStack;

        public TeamIterator(List<OrganizationElement> members) {
        iteratorStack = new Stack<>();
        iteratorStack.push(members.iterator());
        }

        @Override
        public boolean hasNext() {
        boolean hasNext = iteratorStack.peek().hasNext();
        while (!hasNext) {
            iteratorStack.pop();
            if (iteratorStack.isEmpty()) {
            return false;
            }
            hasNext = iteratorStack.peek().hasNext();
        }
        return hasNext;
        }

        @Override
        public OrganizationElement next() {
        OrganizationElement nextElement = iteratorStack.peek().next();
        while (nextElement instanceof Team) {
            Team team = (Team) nextElement;
            Iterator<OrganizationElement> newIterator = team.iterator();
            iteratorStack.push(newIterator);
            nextElement = iteratorStack.peek().next();
        }
        return nextElement;
        }
    }
}
