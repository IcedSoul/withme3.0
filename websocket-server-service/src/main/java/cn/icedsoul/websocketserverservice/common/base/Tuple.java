package cn.icedsoul.websocketserverservice.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author megumin
 */
@Getter
@Setter
@AllArgsConstructor
public class Tuple<X, Y> {
    X first;
    Y second;
}
