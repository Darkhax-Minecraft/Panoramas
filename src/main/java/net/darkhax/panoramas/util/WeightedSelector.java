package net.darkhax.panoramas.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import io.netty.util.internal.ThreadLocalRandom;

public class WeightedSelector<T> {
    
    private final Function<T, Integer> weightResolver;
    private final List<T> entries = new LinkedList<>();
    private int totalWeight;
    
    public WeightedSelector(Function<T, Integer> weightResolver) {
        
        this.weightResolver = weightResolver;
    }
    
    public WeightedSelector<T> addAll (T[] toAdd) {
        
        for (final T newEntry : toAdd) {
            
            this.add(newEntry);
        }
        
        return this;
    }
    
    public WeightedSelector<T> addAll (Collection<T> toAdd) {
        
        for (final T newEntry : toAdd) {
            
            this.add(newEntry);
        }
        
        return this;
    }
    
    public WeightedSelector<T> add (T toAdd) {
        
        this.entries.add(toAdd);
        this.totalWeight += this.weightResolver.apply(toAdd);
        return this;
    }
    
    public WeightedSelector<T> remove (T toRemove) {
        
        if (this.entries.remove(toRemove)) {
            
            this.totalWeight -= this.weightResolver.apply(toRemove);
        }
        
        return this;
    }
    
    public T get () {
        
        return this.get(ThreadLocalRandom.current());
    }
    
    public T get (Random random) {
        
        return this.get(random.nextInt(this.totalWeight));
    }
    
    public T get (int selectedWeight) {
        
        for (final T entry : this.entries) {
            
            selectedWeight -= this.weightResolver.apply(entry);
            
            if (selectedWeight < 0) {
                
                return entry;
            }
        }
        
        return null;
    }
    
    public boolean isEmpty () {
        
        return this.entries.isEmpty();
    }
}