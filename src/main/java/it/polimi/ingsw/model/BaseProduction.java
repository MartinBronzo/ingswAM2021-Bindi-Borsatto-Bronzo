package it.polimi.ingsw.model;

import java.util.*;

/**
 * Implements the BaseProduction Logic
 * cannot require of give as output faithpoints
 */
public class BaseProduction {
    private final int quantityInputResources;
    private final int quantityOutputResources;
    private final List<ResourceType> inputForcedResources;
    private final List<ResourceType> outputForcedResources;
    private HashMap<ResourceType, Integer> inputHashMap;
    private HashMap<ResourceType, Integer> outputHashMap;

    /**
     * Base Constructor for standard rules
     */
    public BaseProduction() {
        quantityInputResources = 2;
        quantityOutputResources = 1;
        inputForcedResources = new LinkedList<>();
        outputForcedResources = new LinkedList<>();
        inputHashMap = new HashMap<>();
        outputHashMap = new HashMap<>();
    }

    /**
     * Creates a copy of the baseProduction parameter
     *
     * @param baseProduction the BaseProduction to be copied
     */
    public BaseProduction(BaseProduction baseProduction) {
        if (baseProduction == null) throw new NullPointerException("baseProduction can't be null");
        this.quantityInputResources = baseProduction.quantityInputResources;
        this.quantityOutputResources = baseProduction.quantityOutputResources;
        this.inputForcedResources = new LinkedList<>(baseProduction.inputForcedResources);
        this.outputForcedResources = new LinkedList<>(baseProduction.outputForcedResources);
        this.inputHashMap = new HashMap<>(baseProduction.inputHashMap);
        this.outputHashMap = new HashMap<>(baseProduction.outputHashMap);
    }

    /**
     * Constructor for not standard rules
     *
     * @param inputForcedResources    the resources which must be present in the Input HashMap. whether a resource must be present more times add it more times here
     * @param outputForcedResources   the resources which must be present in the Output HashMap. whether a resource must be present more times add it more times here
     * @param quantityInputResources  the total quantity of resources in the Input HashMap.
     * @param quantityOutputResources the total quantity of resources in the Output HashMap.
     * @throws NullPointerException     if inputForcedResources or outputForcedResources are null
     * @throws IllegalArgumentException if inputForcedResources.size() > quantityInputResources or outputForcedResources.size() > quantityOutputResources or inputForcedResources contains faithpoints or outputForcedResources contains faithpoints
     */
    public BaseProduction(List<ResourceType> inputForcedResources, List<ResourceType> outputForcedResources, int quantityInputResources, int quantityOutputResources) throws NullPointerException, IllegalArgumentException {
        if (inputForcedResources == null || outputForcedResources == null)
            throw new NullPointerException("Lists can't be null");
        if (inputForcedResources.size() > quantityInputResources || outputForcedResources.size() > quantityOutputResources)
            throw new IllegalArgumentException("Configuration is not Legit");
        if (inputForcedResources.contains(ResourceType.FAITHPOINT) || outputForcedResources.contains(ResourceType.FAITHPOINT))
            throw new IllegalArgumentException("List can't contains faithpoints");

        this.quantityInputResources = quantityInputResources;
        this.quantityOutputResources = quantityOutputResources;
        this.inputForcedResources = new LinkedList<>(inputForcedResources);
        this.outputForcedResources = new LinkedList<>(outputForcedResources);
        this.inputHashMap = new HashMap<>();
        this.outputHashMap = new HashMap<>();
    }

    /**
     * this class codes Parameters in input HashMap and output HashMap which represents the input Cost of the BaseProduction and the output production.
     * Also performs analysis to see if parameters are can be valid for this Base Production
     *
     * @param inputResources  the List which represents the desired cost HashMap
     * @param outputResources the List which represents the desired output HashMap
     * @return true if the lists are Valid for this BaseProduction
     * @throws IllegalArgumentException if one or more parameters are not Valid
     * @throws NullPointerException     if inputResources or outputResources are null
     */
    public boolean setBaseProduction(List<ResourceType> inputResources, List<ResourceType> outputResources) throws IllegalArgumentException, NullPointerException {
        return setInputHashMap(inputResources) && setOutputHashMap(outputResources);
    }

    /**
     * this class codes the input HashMap which represents the input Cost of the BaseProduction.
     * Also performs analysis to see if parameter is valid for this Base Production
     *
     * @param inputResources the List which represents the desired cost HashMap
     * @return true if the list is Valid for this BaseProduction
     * @throws IllegalArgumentException if the parameter is not Valid
     * @throws NullPointerException     if inputResources is null
     */
    public boolean setInputHashMap(List<ResourceType> inputResources) throws IllegalArgumentException, NullPointerException {
        if (!checkInputSet(inputResources)) throw new IllegalArgumentException("Configuration List is not Valid");
        this.inputHashMap = new HashMap<>();
        inputResources.forEach(resource -> this.inputHashMap.put(resource, this.inputHashMap.getOrDefault(resource, 0) + 1));
        return true;
    }

    /**
     * this class codes the output HashMap which represents the production of the BaseProduction.
     * Also performs analysis to see if parameter is valid for this Base Production
     *
     * @param outputResources the List which represents the desired production HashMap
     * @return true if the list is Valid for this BaseProduction
     * @throws IllegalArgumentException if the parameter is not Valid
     * @throws NullPointerException     if outputResources is null
     */
    public boolean setOutputHashMap(List<ResourceType> outputResources) {
        if (!checkOutputSet(outputResources)) throw new IllegalArgumentException("Configuration List is not Valid");
        this.outputHashMap = new HashMap<>();
        outputResources.forEach(resource -> this.outputHashMap.put(resource, this.outputHashMap.getOrDefault(resource, 0) + 1));
        return true;
    }

    private boolean checkInputSet(List<ResourceType> inputResources) throws NullPointerException {
        if (inputResources == null) throw new NullPointerException("baseProduction: Input Resources can't be null");
        return inputResources.size() == quantityInputResources && !inputResources.contains(ResourceType.FAITHPOINT) && this.containsAllWithDuplicates(inputForcedResources, inputResources);
    }

    private boolean checkOutputSet(List<ResourceType> outputResources) throws NullPointerException {
        if (outputResources == null) throw new NullPointerException("baseProduction: Output Resources can't be null");
        return outputResources.size() == quantityOutputResources && !outputResources.contains(ResourceType.FAITHPOINT) && this.containsAllWithDuplicates(outputForcedResources, outputResources);
    }

    private boolean containsAllWithDuplicates(Collection<ResourceType> containedResources, Collection<ResourceType> allResources) throws NullPointerException {
        if (containedResources == null || allResources == null)
            throw new NullPointerException("containsAllWithDuplicates: one parameter is null");
        List<ResourceType> checkerList = new LinkedList<>(allResources);
        for (ResourceType resource : containedResources) {
            if (!checkerList.remove(resource))
                return false;
        }
        return true;
    }

    /**
     * @return the number of resources required as input
     */
    public int getQuantityInputResources() {
        return quantityInputResources;
    }

    /**
     * @return the number of resources required as output
     */
    public int getQuantityOutputResources() {
        return quantityOutputResources;
    }

    /**
     * @return a List of the must Have resources required as Input, an empty List if no resources are required
     */
    public List<ResourceType> getInputForcedResources() {
        return new ArrayList<>(inputForcedResources);
    }

    /**
     * @return a List of the must Have resources required as Output, an empty List if no resources are required
     */
    public List<ResourceType> getOutputForcedResources() {
        return new ArrayList<>(outputForcedResources);
    }

    /**
     * @return the HashMap which represents the cost required to produce resources
     */
    public HashMap<ResourceType, Integer> getInputHashMap() {
        return new HashMap<>(inputHashMap);
    }

    /**
     * @return the HashMap which represents the production
     */
    public HashMap<ResourceType, Integer> getOutputHashMap() {
        return new HashMap<>(outputHashMap);
    }
}
