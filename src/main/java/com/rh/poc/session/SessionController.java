package com.rh.poc.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class SessionController {

    @PostMapping("/set")
    public String setSession(@RequestParam String key, @RequestParam String value, HttpSession session) {
        System.out.println(" set key >>>>>>>>>>>>>>>>>>>>>> "+key);
    	System.out.println("set value >>>>>>>>>>>>>>>>>>>>>> "+value);
        System.out.println("hello "+session);
        session.setAttribute(key, value);
        System.out.println(">>>>>>>>>>>>>>end set >>>>>>>>>>>>>>>>>>>>>>");
        return "Session attribute set: " + key + " = " + value;
    }

    @GetMapping("/get")
    public String getSession(@RequestParam String key, HttpSession session) {
    	System.out.println("get key >>>>>>>>>>>>>>>>>>>>>> "+key);
    	System.out.println(session.getAttributeNames().nextElement());
        Object value = session.getAttribute(key);
        System.out.println(">>>>>>>>>>end get key >>>>>>>>>>>>>>>>>>>>>> "+key);
        return value != null ? "Session attribute: " + key + " = " + value : "No value found";
    }
    
    @DeleteMapping("/invalidate")
    public String invalidateSession(HttpSession session) {
        session.invalidate();
        return "Session invalidated";
    }
}
