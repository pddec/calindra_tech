package com.calindra.tech.backend.functions;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

import  com.calindra.tech.backend.model.Address.AddressBuilder;
import  com.calindra.tech.backend.model.Address.Type;


public class AddressFunctions {

    public static Consumer<Type> eachTypes(final AddressBuilder builder){
        return type -> type.buildAddress(builder);
    }
    
    
    public static BiFunction<StringBuilder,String,StringBuilder> accomulatorTypes(){
        return (sb, type) -> sb.append(type);
    }
    
    public static BinaryOperator<StringBuilder> combinatorTypes(){
        return (oldSb,newSb) -> oldSb.append(newSb.toString());
    }
}
