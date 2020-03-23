package com.atguigu.secondKill;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.Set;

@Controller
public class SecondKillController {

    static String secKillScript ="local userid=KEYS[1];\r\n" +
            "local prodid=KEYS[2];\r\n" +
            "local qtkey='sk:'..prodid..\":qt\";\r\n" +
            "local usersKey='sk:'..prodid..\":usr\";\r\n" +
            "local userExists=redis.call(\"sismember\",usersKey,userid);\r\n" +
            "if tonumber(userExists)==1 then \r\n" +
            "   return 2;\r\n" +
            "end\r\n" +
            "local num= redis.call(\"get\" ,qtkey);\r\n" +
            "if tonumber(num)<=0 then \r\n" +
            "   return 0;\r\n" +
            "else \r\n" +
            "   redis.call(\"decr\",qtkey);\r\n" +
            "   redis.call(\"sadd\",usersKey,userid);\r\n" +
            "end\r\n" +
            "return 1" ;

    static String secKillScript2 =
            "local userExists=redis.call(\"sismember\",\"{sk}:0101:usr\",userid);\r\n" +
                    " return 1";


    @ResponseBody
    @PostMapping("/doSecondKill")
    public String diSecondKill(String prodid){

        String userid = (int)(10000*Math.random())+"";
        Jedis jedis = new Jedis("192.168.225.138", 6379);

        String scriptLoad = jedis.scriptLoad(secKillScript);

        Object eval = jedis.evalsha(scriptLoad, 2, userid,prodid);

        String reString = String.valueOf(eval);
        if ("0".equals( reString )  ) {
 					System.err.println("已抢空！！");
				}else if("1".equals( reString )  )  {
 					System.out.println("抢购成功！！！！");
				}else if("2".equals( reString )  )  {
 					System.err.println("该用户已抢过！！");
				}else{
 					System.err.println("抢购异常！！");
				}
        jedis.close();
        return "seccuess";




     /*   //判断用户是否参与过秒杀
        Boolean flag = jedis.sismember(userKey, userid);
        if(flag){
            return "10000";//用户参与过秒杀活动
        }
        //判断秒杀商品是否库存充足

        String qt = jedis.get(prodqtKey);
        if(qt == null){
            return "20000";//还没开始秒杀活动
        }

        int qts = Integer.parseInt(qt);
        if(qts <= 0){
            return "30000";//库存不足
        }

        //秒杀成功，减库存，将秒杀成功的用户添加到秒杀成功的用户里面

        jedis.decr(prodqtKey);
        jedis.sadd(userKey, userid);
        jedis.close();



        //给用户成功响应

        System.out.println("userid = " + userid+",prodid = "+prodid);


        return "seccuess";*/
    }
}
