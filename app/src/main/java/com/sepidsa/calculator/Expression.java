package com.sepidsa.calculator;

import android.content.Context;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.StringTokenizer;

public class  Expression{
    private final Context mContext;
    /*
        * Strings used for storing expression.
        */
    private boolean PERCENT_FLAG = false;
    private boolean mDeg_flag;
    public String s, x;
    public BigDecimal last;
    private String mTest;
    /*
    * Term evaluator for number literals.
    */

    BigDecimal term(){
        BigDecimal ans = null;
        StringBuffer temp = new StringBuffer("");

        boolean neg = false;
        if( s.charAt(0) == '−' || s.charAt(0) == '-' ){
            neg = true;
            s = s.substring( 1 );
        }

        if( s.length() > 0 && s.charAt( 0 ) == 'e') {
            s = s.substring(1);
            ans =new BigDecimal( Math.E);
        } else if( s.length() > 0 && s.charAt( 0 ) == 'π'){
            s = s.substring( 1 );
            ans =new BigDecimal( Math.PI);

        }else{
            while( s.length() > 0 && Character.isDigit( s.charAt( 0 ) ) ){
                temp.append(Integer.parseInt( "" + s.charAt( 0 ) ));
                s = s.substring( 1 );
            }
            if( s.length() > 0 && s.charAt( 0 ) == '.' ){
                temp.append( '.' );
                s = s.substring( 1 );
                while( s.length() > 0 && Character.isDigit( s.charAt( 0 ) ) ){
                    temp.append(Integer.parseInt( "" + s.charAt( 0 ) ));
                    s = s.substring( 1 );
                }
            }
            if( s.length() > 0 && (s.charAt(0) == 'e' || s.charAt(0) == 'E') ){
                temp.append( 'e' );
                s = s.substring( 1 );
                temp.append( s.charAt( 0 ) );
                s = s.substring( 1 );
                while( s.length() > 0 && Character.isDigit( s.charAt( 0 ) ) ){
                    temp.append(Integer.parseInt( "" + s.charAt( 0 ) ));
                    s = s.substring( 1 );

                }
            }
           /* if(s.length() > 0 && (s.charAt(0) == ')')){
                ans = (double)1;
                return ans;

            }
*/
            if(temp.equals("") == true) {
                return new BigDecimal(0);

            }else {
                ans = new BigDecimal(temp.toString());

            }
//            if(neg ){
//                if(ans != null){
//                    ans = ans.negate();
//                }else {
//                    ans = new BigDecimal(-1);
//                }
//            }
        }
        return ans;
    }




    /*public BigDecimal compute_factorial(BigDecimal n) {
        double[] fact = {1, 1, 2, 6, 24, 120, 720, 5040, 40320,
                362880, 3628800, 39916800, 479001600};
        return fact[n.intValue()];
    }
*/



    static BigDecimal compute_factorial(BigDecimal n, BigDecimal acc) {
        if (n.equals(BigDecimal.ONE)) {
            return acc;
        }
        BigDecimal lessOne = n.subtract(BigDecimal.ONE);
        return compute_factorial(lessOne, acc.multiply(lessOne));
    }



    BigDecimal paren(){
        BigDecimal ans = null;

        if(s.length()!=0)   {
            if (s.charAt(0) == '(') {
                s = s.substring(1);
                ans = add();
                if (s.length()>0 && s.charAt(0) == ')') {
                    s = s.substring(1);
                }
            }else {
                ans = term();
            }
        }
        return ans;
    }


    BigDecimal factorial(){
        BigDecimal ans = paren();


        while( s.length() > 0 ){
            if (s.indexOf(mTest) == 0) {
                s = s.substring(1);
                ans = BigDecimalUtils.intPower(ans, 2, 6);
                break;
            }
            else if (s.indexOf(mContext.getResources().getString(R.string.powerthreeTag)) == 0) {
                s = s.substring(1);
                ans = BigDecimalUtils.intPower(ans, 3, 6);
                break;
            }
            if( s.charAt( 0 ) == '!' ) {
                s = s.substring(1);
                ans = compute_factorial(ans.round(new MathContext(6)),ans.round(new MathContext(6)));
                //todo
            }else if ( s.charAt( 0 ) == '%'){
                PERCENT_FLAG = true;
                //s = s.substring(1);
                break;
            }else {
                break;
            }
        }
        return ans;
    }



    /*
	* Exponentiation solver.
	*/
    BigDecimal exp(){
        boolean neg = false;
        if(  s.charAt( 0 ) == '−' || s.charAt(0) == '-'  ){
            neg = true;
            s = s.substring( 1 );
            return new BigDecimal(-1);
        }
        BigDecimal ans = factorial();
        while( s.length() > 0 ){
            if( s.indexOf( "^" ) == 0 ){
                s = s.substring( 1 );
                boolean expNeg = false;
                // Checking with both types of minus charecter (they look the same here but in real life they look diff
                if( s.charAt( 0 ) == '−' || s.charAt(0) == '-' ){
                    expNeg = true;
                    s = s.substring( 1 );
                }
                BigDecimal e = factorial();

                if( ans.doubleValue() < 0 ){		// if it's negative
                    BigDecimal x = new BigDecimal("1");
                    if( Math.ceil(e.doubleValue()) == e.doubleValue() ){	// only raise to an integer
                        if( expNeg )
                            e = e.multiply(new BigDecimal("-1"));
                        if( e.doubleValue() == 0 )
                            ans= new BigDecimal("1");
                        else if( e.doubleValue() > 0 )
                            for( int i = 0; i < e.doubleValue(); i++ )
                                x = x.multiply( ans );
                        else
                            for( int i = 0; i < -e.doubleValue(); i++ )
                                x = x.divide(ans,new MathContext(6));
                        ans = x;
                    } else {
                        ans = new BigDecimal(Math.log(-1));	// otherwise make it NaN
                    }
                }
                else if( expNeg ) {
//                    ans = new BigDecimal(Math.exp(e.multiply(new BigDecimal(Math.log(ans.doubleValue()))).negate().doubleValue()));
//                    ans = ans.pow(e.negate().intValue()) ;
                    ans = BigDecimalUtils.intPower(ans,e.negate().intValue(),6);

                }
                else{
//                    ans = new BigDecimal( Math.exp( e.multiply(new BigDecimal( Math.log(ans.doubleValue()) )).doubleValue())) ;
                    ans = ans.pow(e.intValue()) ;
//                    ans =   BigFunctions.exp( BigFunctions.ln(e,100).multiply(ans),100 );
                }
            } else
                break;
        }
        if( neg )
            ans = ans.negate();
        return ans;
    }


    /*
    * Trigonometric function solver.
    */
    BigDecimal trig(){
        BigDecimal ans = new BigDecimal("0");
        boolean found = false;
        if(s.length() > 0 ) {
            if (s.indexOf("sinh") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.sinh(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(Math.sinh(Math.toRadians(trig().doubleValue())));
                }
                found = true;
            } else if (s.indexOf("sin") == 0) {
                s = s.substring(3);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.sin(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(Math.sin(Math.toRadians(trig().doubleValue())));
                }
                found = true;
            } else if (s.indexOf("csc") == 0) {
                s = s.substring(3);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.pow(Math.sin(trig().doubleValue()), -1));
                } else {
                    ans = new BigDecimal(Math.pow(Math.sin(Math.toRadians(trig().doubleValue())), -1));
                }
                found = true;
            } else if (s.indexOf("asin") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.asin(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(Math.asin(Math.toRadians(trig().doubleValue())));
                }
                found = true;
            } else if (s.indexOf("asinh") == 0) {
                s = s.substring(5);
                ans = trig();
                double doubleAns = ans.doubleValue();
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.log(doubleAns + Math.sqrt(Math.pow(doubleAns, 2) + 1.0)));
                } else {
                    ans = new BigDecimal(Math.log(Math.toRadians(doubleAns) + Math.sqrt(Math.pow(Math.toRadians(doubleAns), 2) + 1.0)));
                    // todo what for radians
                }
                found = true;
            }

//        Math.log(x + Math.sqrt(x*x + 1.0));
            //CSC is 1/sin(X)
            else if (s.indexOf("acsc") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    //1/asin
                    ans = new BigDecimal(Math.pow(Math.asin(trig().doubleValue()), -1));
                } else {
                    ans = new BigDecimal(Math.pow(Math.asin(Math.toRadians(trig().doubleValue())), -1));
                }
                found = true;
            } else if (s.indexOf("acsch") == 0) {
                // = asinh (1/x)
                s = s.substring(5);
                ans = trig();
                double doubleAns = 1 / ans.doubleValue();
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.log(doubleAns + Math.sqrt(Math.pow(doubleAns, 2) + 1.0)));
                } else {

                    ans = new BigDecimal(Math.log( Math.toRadians(doubleAns) + Math.sqrt(Math.pow( Math.toRadians(doubleAns), 2) + 1.0)));
                    // todo what for radians
                }
                found = true;


            } else if (s.indexOf("cosh") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.cosh(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(Math.cosh(Math.toRadians(trig().doubleValue())));
                }
                found = true;
            } else if (s.indexOf("acosh") == 0) {
                s = s.substring(5);
                ans = trig();
                double doubleAns = ans.doubleValue();
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.log(doubleAns + Math.sqrt(Math.pow(doubleAns, 2) - 1.0)));
                } else {
                    ans = new BigDecimal(Math.log(Math.toRadians(doubleAns) + Math.sqrt(Math.pow(Math.toRadians(doubleAns), 2) - 1.0)));
                    // todo what for radians
                }
                found = true;
            } else if (s.indexOf("cos") == 0) {
                s = s.substring(3);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.cos(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(Math.cos(Math.toRadians(trig().doubleValue())));
                }
                found = true;
            } else if (s.indexOf("sec") == 0) {
                s = s.substring(3);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.pow(Math.cos(trig().doubleValue()), -1));
                } else {
                    ans = new BigDecimal(Math.pow(Math.cos(Math.toRadians(trig().doubleValue())), -1));
                }
                found = true;
            } else if (s.indexOf("acos") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.acos(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(Math.acos(Math.toRadians(trig().doubleValue())));
                }
                found = true;
            } else if (s.indexOf("asec") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.pow(Math.acos(trig().doubleValue()), -1));
                } else {
                    ans = new BigDecimal(Math.pow(Math.acos(Math.toRadians(trig().doubleValue())), -1));
                }
                found = true;
            } else if (s.indexOf("asech") == 0) {
                s = s.substring(5);
                ans = trig();
                double doubleAns = 1 / ans.doubleValue();
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.log(doubleAns + Math.sqrt(Math.pow(doubleAns, 2) - 1.0)));
                } else {
                    ans = new BigDecimal(Math.log(Math.toRadians(doubleAns) + Math.sqrt(Math.pow(Math.toRadians(doubleAns), 2) - 1.0)));
                    // todo what for radians
                }
                found = true;
            } else if (s.indexOf("tanh") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.tanh(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(Math.tanh(Math.toRadians(trig().doubleValue())));
                }
                found = true;
            } else if (s.indexOf("atanh") == 0) {
                s = s.substring(5);
                ans = trig();
                double doubleAns = ans.doubleValue();
                if (mDeg_flag) {
//                return 0.5*Math.log( (x + 1.0) / (x - 1.0) );
                    ans = new BigDecimal(0.5 * Math.log((doubleAns + 1.0) / (doubleAns - 1.0)));
                } else {
                    ans = new BigDecimal(0.5 * Math.log((Math.toRadians(doubleAns) + 1.0) / (Math.toRadians(doubleAns) - 1.0)));
                    // todo what for radians
                }
                found = true;
            } else if (s.indexOf("tan") == 0) {
                s = s.substring(3);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.tan(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(Math.tan(Math.toRadians(trig().doubleValue())));
                }
                found = true;

            } else if (s.indexOf("atan") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    ans = new BigDecimal(Math.atan(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(Math.atan(Math.toRadians(trig().doubleValue())));
                }
                found = true;

            } else if (s.indexOf("coth") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    ans = new BigDecimal(1.0 / Math.tanh(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(1.0 / Math.tanh(Math.toRadians(trig().doubleValue())));
                }
                found = true;
            } else if (s.indexOf("cot") == 0) {
                s = s.substring(3);
                if (mDeg_flag) {
                    ans = new BigDecimal(1.0 / Math.tan(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(1.0 / Math.tan(Math.toRadians(trig().doubleValue())));
                }
                found = true;

            } else if (s.indexOf("acot") == 0) {
                s = s.substring(4);
                if (mDeg_flag) {
                    ans = new BigDecimal(1.0 / Math.atan(trig().doubleValue()));
                } else {
                    ans = new BigDecimal(1.0 / Math.atan(Math.toRadians(trig().doubleValue())));
                }
                found = true;

            } else if (s.indexOf("acoth") == 0) {
                s = s.substring(5);
                ans = trig();
                double doubleAns = 1 / ans.doubleValue();
                if (mDeg_flag) {
//                return 0.5*Math.log( (x + 1.0) / (x - 1.0) );
                    ans = new BigDecimal(0.5 * Math.log((doubleAns + 1.0) / (doubleAns - 1.0)));
                } else {

                    ans = new BigDecimal(0.5 * Math.log((Math.toRadians(doubleAns) + 1.0) / (Math.toRadians(doubleAns) - 1.0)));
                    // todo what for radians
                }
                found = true;
            } else if (s.indexOf("log") == 0) {
                s = s.substring(3);
                ans = new BigDecimal(Math.log10(trig().doubleValue()));
                found = true;
            } else if (s.indexOf("ln") == 0) {
                s = s.substring(2);
                ans = BigDecimalUtils.ln(trig(), 6);
                found = true;
            } else if (s.charAt(0) == '\u221a') {
                s = s.substring(1);
                ans = BigDecimalUtils.sqrt(trig(), 6);
                found = true;

            }
        }


        if( !found ){
            ans = exp();
        }
        return ans;
    }


    /*
    * Multiplication, division expression solver.
    */
    BigDecimal mul(){
        BigDecimal tempAnswer = new BigDecimal("0");
        BigDecimal ans = trig();


        if( s.length() > 0 ){

//            For 20% * 3
            if((s.length()>1 && s.charAt(1) == '\u00d7') )
                if( (s.charAt(0) == '%'))
                {
                    PERCENT_FLAG = false;
                    ans = ans.divide(new BigDecimal("100"),new MathContext(6));
                    s = s.substring( 1 );
                }

            while( s.length() > 0 ){

                if( s.charAt( 0 ) == '\u00d7' || s.charAt( 0 ) == '*' ){
                    s = s.substring( 1 );

                    tempAnswer = trig();

                    if(PERCENT_FLAG == true){
                        ans = (ans.divide(new BigDecimal("100"),new MathContext(6)).multiply(tempAnswer));
                        PERCENT_FLAG = false;
                        tempAnswer = new BigDecimal(0);
                        s = s.substring( 1 );
                    }else {
                        // if(s.length()>0)
                        ans = ans.multiply( tempAnswer);

                    }


                } else if( s.charAt( 0 ) == '/'|| s.charAt( 0 ) == '÷' ) {
                    s = s.substring(1);
                    tempAnswer = trig();

                    if (PERCENT_FLAG == true) {
                        ans = (ans.multiply(new BigDecimal("100"),new MathContext(6)).divide(tempAnswer));
                        PERCENT_FLAG = false;
                        tempAnswer = new BigDecimal(0);
                        s = s.substring( 1 );
                    }
                    else {
                        //  tempAnswer = trig();
                        // if(s.length()>0)
                        ans = ans.divide( tempAnswer,new MathContext(6));
                    }
                }


                //2 % 300
                // 2 * 3%
                // 20% + 2
                // 2 + 10%
                // 2%



                else{
                    //  s= s.substring( 1 );
                    if (s.charAt(0) != '+'
                            && s.charAt(0) != '−'
                            && s.charAt(0) != ')'
                            && s.charAt(0) != '%') {

                        ans = ans.multiply(mul());
                    }


                    break;
                }
            }
        }
        return ans;
    }

    /*
    * Addition, subtraction expression solver.
    */
    BigDecimal add(){
        BigDecimal tempAnswer = new BigDecimal("0");
        BigDecimal ans = mul();

        // for 20% + 200
        if((s.length()== 1  ) ||
                (s.length()>1 &&( (s.charAt(1) == '+')||(s.charAt(1) == '\u00d7')
                        ||(s.charAt(1) == '−') || (s.charAt(1) == '÷'))  ) )
            if( (s.charAt(0) == '%'))
            {
                PERCENT_FLAG = false;
                ans = ans.divide(new BigDecimal("100"),new MathContext(6));
                s = s.substring( 1 );
            }


        while( s.length() > 0 ){

            if( s.charAt( 0 ) == '+' )
            {
                s = s.substring( 1 );
                tempAnswer = mul();

                // for 200 + 2%
                if(PERCENT_FLAG == true){
                    ans = ans.add((ans.multiply(tempAnswer)).divide(new BigDecimal("100"),new MathContext(6)));
                    PERCENT_FLAG = false;
                    s = s.substring( 1 );
                }else {
                    // if(s.length()>0)
                    ans = ans.add( tempAnswer);
                }

            } else if( s.charAt( 0 ) == '−' || s.charAt(0) == '-' ){
                s = s.substring( 1 );

                // for 200 - 2%
                tempAnswer = mul();
                if(PERCENT_FLAG == true){
                    ans = ans.subtract((ans.multiply( tempAnswer)).divide(new BigDecimal("100"),new MathContext(6)));
                    PERCENT_FLAG = false;
                    s = s.substring( 1 );
                    tempAnswer = new BigDecimal(0);
                }else {
                    // if(s.length()>0)
                    ans = ans.subtract( tempAnswer);
                }
            }

            // for 2 % 200
            else if(s.charAt( 0 ) == '%' ){
                s = s.substring( 1 );
                tempAnswer = mul();
                // if(s.length()>0)
                ans = (ans.divide(new BigDecimal("100"),new MathContext(6)).multiply(tempAnswer));
                PERCENT_FLAG = false;
            }

            else{
                // 2%*5
                /*if( s.charAt( 0 ) != ')'){
                    // s = s.substring( 1 );
                    ans = ans.multiply( mul());
                    PERCENT_FLAG = false;


                }else {
                    break;
                }
*/
                break;
            }
        }
        return ans;
    }




    /*
    * Public access method to evaluate this expression.
    */
    public BigDecimal evaluate(){
        s = x.intern();
        s= s.replace(",","");
        last = add();
        return last;
    }

    /*
    * Creates new Expression.
    */
    public Expression(String s, boolean _isRad_flag, Context applicationContext){
        // remove white space, assume only spaces or tabs
        mContext = applicationContext;
        mTest = mContext.getResources().getString(R.string.powertwoTag);
        int i = mTest.length();
        mDeg_flag = _isRad_flag;
        StringBuffer b = new StringBuffer();
        StringTokenizer t = new StringTokenizer( s, " " );
        while( t.hasMoreElements() )
            b.append( t.nextToken() );
        t = new StringTokenizer( b.toString(), "\t" );
        b = new StringBuffer();
        while( t.hasMoreElements() )
            b.append( t.nextToken() );

        x = b.toString();
    }

    /*
    * The String value of this Expression.
    */
    public String toString(){
        return x.intern();
    }

    /*
    * Test our Expression class by evaluating the command-line
    * argument and then returning.
    */

}