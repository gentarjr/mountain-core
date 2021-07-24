package com.mountain.library.domain;

import com.mountain.library.helper.NumberUtils;

import java.math.BigDecimal;
import java.util.List;

public class BigDecimalBuilder {

    private BigDecimal number = BigDecimal.ZERO;

    public BigDecimalBuilder() {
    }

    public BigDecimalBuilder(BigDecimal number) {
        this.number = number;
    }

    public BigDecimalBuilder add(BigDecimal add){
        number = NumberUtils.add(number, add);
        return this;
    }

    public BigDecimalBuilder add(double add){
        number = add(new BigDecimal(add)).build();
        return this;
    }

    public BigDecimalBuilder substract(BigDecimal substract){
        number = NumberUtils.substract(number, substract);
        return this;
    }

    public BigDecimalBuilder substract(double substract){
        number = substract(new BigDecimal(substract)).build();
        return this;
    }

    public BigDecimalBuilder times(BigDecimal times){
        number = NumberUtils.times(number, times);
        return this;
    }

    public BigDecimalBuilder times(double times){
        number = times(new BigDecimal(times)).build();
        return this;
    }

    public BigDecimalBuilder divide(BigDecimal divisor){
        number = NumberUtils.divide(number, divisor);
        return this;
    }

    public BigDecimalBuilder divide(double divisor){
        number = divide(new BigDecimal(divisor)).build();
        return this;
    }

    public BigDecimalBuilder sum(List<BigDecimal> daftarNumber){
        if(daftarNumber == null){
            throw new NullPointerException("Argument tidak boleh null");
        }
        number = daftarNumber
                .stream()
                .reduce(number, (identity, n) -> NumberUtils.add(identity, n));
        return this;
    }

    public BigDecimal build(){
        return number;
    }

    public String buildFormatted(){
        return NumberUtils.formatCurrency(number);
    }
}
