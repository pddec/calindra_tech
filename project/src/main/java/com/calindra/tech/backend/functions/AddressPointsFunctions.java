package com.calindra.tech.backend.functions;

import com.calindra.tech.backend.model.AddressPoints;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AddressPointsFunctions {


    private static Function<AddressPoints, AddressPoints > calcPoints (final AddressPoints current){
        return addressPoint -> {
            final Double distance = current.calcDistance(addressPoint);

            return addressPoint.toBuilder()
                    .distance(distance)
                    .build();
        };
    }

    public static Comparator<AddressPoints> comparatorDist (){
        return new Comparator<AddressPoints>() {
            @Override
            public int compare(AddressPoints ap1, AddressPoints ap2) {
                if(ap1.getDistance() > ap2.getDistance())
                    return -1;
                if(ap1.getDistance() < ap2.getDistance())
                    return 1;
                return 0;
            }
        };
    }

    public static Function<AddressPoints, AddressPoints> calcDistances (final List<AddressPoints> addressPoints) {
        return current ->{
            final List<AddressPoints> pointsList = addressPoints.stream()
                    .map(AddressPointsFunctions.calcPoints(current))
                    .sorted(AddressPointsFunctions.comparatorDist())
                    .collect(Collectors.toList());

            return current.toBuilder()
                    .addressPoints(pointsList)
                    .build();
        };
    }


}
