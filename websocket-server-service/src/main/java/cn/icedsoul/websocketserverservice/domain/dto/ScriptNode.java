package cn.icedsoul.websocketserverservice.domain.dto;

import cn.icedsoul.websocketserverservice.common.base.Tuple;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 脚本节点，记录了每个节点的作用与文本
 * @Author megumin
 */
@Data
@Builder
public class ScriptNode {

    /**
     * 旁白
     */
    public static final String NARRATIVE = "N";

    /**
     * 选项
     */
    public static final String CHOICE = "C";

    /**
     * 开始
     */
    public static final String START = "S";

    /**
     * 结局
     */
    public static final String END = "E";

    final NodeType type;

    /**
     * Narrative: Tuple<content, target>
     */
    final List<Tuple<String, String>> contents;

    /**
     * 脚本节点类型，分四种：
     * N: 旁白，打印完旁白之后跳转指定节点
     * C: 选项，给定数个选项，指定选项的跳转节点
     * S: 开始，游戏开始的地方，和旁白一样的作用
     * E: 结局，打印完结局之后
     */
    public enum NodeType {

        /**
         * 旁白
         */
        NARRATIVE(ScriptNode.NARRATIVE),

        /**
         * 选项
         */
        CHOICE(ScriptNode.CHOICE),

        /**
         * 开始
         */
        START(ScriptNode.START),

        /**
         * 结局
         */
        END(ScriptNode.END);

        /**
         * 脚本中的指定字符
         */
        private String key;

        /**
         * set key
         * @param key
         */
        private NodeType(String key) {
            this.key = key;
        }

        /**
         * get key
         * @return
         */
        public String getKey() {
            return key;
        }
    }
}
