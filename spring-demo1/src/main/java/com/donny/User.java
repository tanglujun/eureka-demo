
package com.donny;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 功能说明
 * <p>
 *
 * @author 唐陆军
 * @version 1.0.0
 * @date 2020/12/17
 */
@Data
public class User {

    private String name;

    private Integer age;

    @Autowired
    private Role role;
}
