package fr.uvsq.hal.pglp.rpg;
import java.util.*;
/**
 * La classe <code>Character</code> représente un personnage de JdR.
 *
 * @author hal
 * @version 2022
 */
public class Character implements OrganizationElement{

    private final String name;
    private final Ability[] prefereAbilityOrdre;
    private int[] AbilityValue;
    private int[] Modificateur;
    private int[] ProficiencyBonus;

    private Character(CharacterBuilder builder) {
        this.name = builder.name;
        this.prefereAbilityOrdre = builder.prefereAbilityOrdre;
        this.AbilityValue = builder.AbilityValue;
        this.Modificateur = builder.Modificateur;
        this.ProficiencyBonus = builder.ProficiencyBonus;
    }

    public String getname() {
        return this.name;
    }

    /**
     * Définissez des accesseurs pour récupérer, 
     * pour chaque caractéristique, sa valeur (getScore(Ability ability)) 
     * ainsi que son modificateur (getModifier(Ability ability))
     * @return valeur des caracteristique
     * @return modificateur
     */
    public int getScore(Ability ability) {
        for (int i = 0; i < 6; i++){
            if (this.prefereAbilityOrdre[i] == ability){
                return this.AbilityValue[i];
            }
        }
        return 0;       //bug, si on trouve pas cette ability
    }
    public int getModifier(Ability ability){
        for (int i = 0; i < 6; i++){
            if (this.prefereAbilityOrdre[i] == ability){
                return this.Modificateur[i];
            }
        }
        return -10;     //bug, si on trouve pas cette ability
    }

    /**
     * Définissez la méthode (abilityCheck) effectuant un jet de caractéristique.
     * Cette méthode prendra en paramètre une caractéristique et un degré de difficulté. 
     * L’opération consiste à tirer un nombre aléatoire entre 1 et 20 et à y ajouter le modificateur de caractéristique. 
     * Si le résultat est supérieur ou égal au degré de difficulté, la méthode doit retourner true. 
     * Si le résultat est inférieur au degré de difficulté, la méthode doit retourner false.
     * 
     */
    public Boolean abilityCheck(Ability ability, int degre) {
        Random random = new Random();
        int n = random.nextInt(20) + 1;   //un nombre aléatoire entre 1 et 20
        for (int i = 0; i < 6; i++){
            if (this.prefereAbilityOrdre[i] == ability) n = n + this.Modificateur[i];
        }
        if(n >= degre){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Définissez la méthode (getSkillProficiency) 
     * retournant @return le bonus pour une compétence donnée en paramètre.
     * Ce bonus est égal à la somme du modificateur de caractéristique associé à la compétence 
     *                           et du bonus de maîtrise si la compétence est définie pour ce personnage. 
     * Si le personnage ne maîtrise pas la compétence, 
     * le bonus de compétence est simplement égal au modificateur de caractéristique associé à la compétence.
     * 
     * Acrobaties:Dexterity, Arcanes :Intelligence
     */
    public int getSkillProficiency(Skill s) {
        int sum = 0;
        if (s == Skill.Acrobatics){
            for (int i = 0; i < 6; i++){
                if (this.prefereAbilityOrdre[i] == Ability.Dexterity){
                    sum = this.Modificateur[i] + this.ProficiencyBonus[i];
                }
            }
        }else if (s == Skill.Arcana){
            for (int i = 0; i < 6; i++){
                if (this.prefereAbilityOrdre[i] == Ability.Intelligence){
                    sum = this.Modificateur[i] + this.ProficiencyBonus[i];
                }
            }
        }
        return sum;
    }

    /**
     * Définissez la méthode (skillCheck) effectuant un jet de compétence.
     * Cette méthode prendra en @param s compétence et @param degre degré de difficulté. 
     * 
     * L’opération consiste à tirer un nombre aléatoire entre 1 et 20 
     * et à y ajouter la valeur du bonus de la compétence. 
     * 
     * Si le résultat est supérieur ou égal au degré de difficulté, la méthode doit retourner @return true. 
     * Si le résultat est inférieur au degré de difficulté, la méthode doit retourner @return false.
     * 
     */
    public boolean skillCheck(Skill s, int degre) {
        Random random = new Random();
        int n = random.nextInt(20) + 1;
        n = n + getSkillProficiency(s);
        
        if(n >= degre){
            return true;
        }else{
            return false;
        }
    }


        
    /**
    * Permet l'initialisation d'un personnel selon le pattern Builder.
    */
    public static class CharacterBuilder {
        // Attributs obligatoires
        private final String name;       
        private final Ability[] prefereAbilityOrdre;         //? peut modifier par nonRandomAbilities() ??  -->suppose non

        private int[] AbilityValue;
        private int[] Modificateur;

        // Attributs optionnels
        private int[] ProficiencyBonus;   //Un tableau indiquant le bonus de chaque ability selon l'ordre de 'prefereAbilityOrdre'.

        public CharacterBuilder(String n, Ability[] preAbility){
            this.name = n;
            this.prefereAbilityOrdre = preAbility;
            this.AbilityValue = genererAllCharacterValeur();
            this.Modificateur = calculeModificateur();
            this.ProficiencyBonus = new int[]{0,0,0,0,0,0};
        }

        public Character build() {
            return new Character(this);
        }

        /**
         * la méthode nonRandomAbilities 
         * attribuer les valeurs 15, 14, 13, 12, 10, 8 selon l’ordre de préférence en paramètre.
         */
        public CharacterBuilder nonRandomAbilities(){
            this.AbilityValue = new int[]{15, 14, 13, 12, 10, 8};
            this.Modificateur = calculeModificateur();
            return this;
        }

        /**
         * une méthode setAbilityScore qui permettra d’attribuer une valeur spécifique à une caractéristique
         */
        public CharacterBuilder setAbilityScore(Ability a, int valeur){
            for (int i = 0; i < 6; i++){
                if (this.prefereAbilityOrdre[i] == a) this.AbilityValue[i] = valeur;
            }
            return this;
        }

        /**
         * une méthode setProficiencyBonus qui permettra de fixer le bonus de maîtrise du personnage
         * Un seul bonus pour un Ability peut être défini à la fois
         */
        public CharacterBuilder setProficiencyBonus(Ability ability, int bonus) {
            for (int i = 0; i < 6; i++){
                if (this.prefereAbilityOrdre[i] == ability) this.ProficiencyBonus[i] = bonus;
            }
            return this;
        }

        
        
        /**
         * Chaque caractéristique est générée en tirant 4 valeurs aléatoires entre 1 et 6
         * dont on conservera la somme des 3 plus élevées.
         * Une valeur de caractéristique comprise entre 2 et 20
         * 
         * @return un characteristique valeur
         */
        private int genererOneCharacterValeur() {
            int sum = 0;
            while(sum<2||sum>20){
                Random random= new Random();
                int [] a = new int[4];
                int i;
                int max = 7;
                int min = 1;
                for(i = 0; i < 4; i++)
                {
                    a[i] = random.nextInt(max)%(max-min)+min;	
                }
                Arrays.sort(a);
                sum = a[1] + a[2] + a[3];
            }
            return sum;
        }

        /**
         * répété tant que la somme des valeurs de caractéristique n’est pas comprise entre 60 et 80.
         * @return tableau de 6 character generer
         */
        private int[] genererAllCharacterValeur() {
            int[] tab = new int[6];
            int sum = 0;
            while (sum<60 || sum>80){    
                for(int i = 0; i < 6; i++){
                    tab[i] = genererOneCharacterValeur();
                }
                sum = Arrays.stream(tab).sum();
            }
            Arrays.sort(tab);
            int[] tab_reverse = new int[6];
            int j = 6;
            for (int i = 0; i < 6; i++) {
                tab_reverse[j - 1] = tab[i];
                j = j - 1;
            }
            return tab_reverse;
        }

        /**
         * la valeur d'un modificateur
         * soustrayez 10 à la valeur, et divisez le résultat par 2, en conservant la partie entière inférieure. 
         * Inscrivez chaque modificateur à côté de la caractéristique correspondante.
         * 
         * @return le table des modificateurs
         */
        private int[] calculeModificateur() {
            int[]modif = new int[6];
            for (int i=0; i < 6; i++){
                modif[i] = (this.AbilityValue[i] - 10)/2; 
            }
            return modif;
        }

    }

    //Methode pour stocker les valeurs dans le bdd
    public Ability getAbility1() {
        return prefereAbilityOrdre[0];
    }
    public Ability getAbility2() {
        return prefereAbilityOrdre[1];
    }
    public Ability getAbility3() {
        return prefereAbilityOrdre[2];
    }
    public Ability getAbility4() {
        return prefereAbilityOrdre[3];
    }
    public Ability getAbility5() {
        return prefereAbilityOrdre[4];
    }
    public Ability getAbility6() {
        return prefereAbilityOrdre[5];
    }
    public int getProficiencyBonus(Ability a) {
        for (int i = 0; i < 6; i++){
            if (this.prefereAbilityOrdre[i] == a){
                return this.AbilityValue[i];
            }
        }
        return 0; 
    }
    
}
