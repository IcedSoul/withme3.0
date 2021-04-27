package cn.icedsoul.websocketserverservice.util;


import cn.icedsoul.websocketserverservice.common.base.Tuple;
import cn.icedsoul.websocketserverservice.domain.dto.ScriptNode;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScriptReaderUtilTest {
    public static final String NEW_GRAD_TEST_SCRIPT = "src/test/resources/newgrad.txt";
    public static final String CEO_TEST_SCRIPT = "src/test/resources/ceo.txt";
    public static final String CAT_TEST_SCRIPT = "src/test/resources/cat.txt";
//    public static final String NEW_GRAD_TEST_SCRIPT = "src/test/resources/newgrad.txt";

    @Test
    public void scriptReaderUtilTestBaseParserFunctionCorrect() throws Exception {
        final HashMap<String, ScriptNode> scriptNodeHashMap = ScriptReaderUtil.parseScript(NEW_GRAD_TEST_SCRIPT);
        assertEquals(scriptNodeHashMap.size(), 40);
    }

    public static void main(String[] args) throws Exception {
        final HashMap<String, ScriptNode> scriptNodeHashMap = ScriptReaderUtil.parseScript("websocket-server-service/" + CAT_TEST_SCRIPT);
        Optional<ScriptNode> stateOptional = scriptNodeHashMap.values().stream()
                .filter(node -> ScriptNode.NodeType.START.equals(node.getType()))
                .findFirst();
        if (stateOptional.isPresent()) {
            ScriptNode currentNode = stateOptional.get();
            boolean run = true;
            while (run) {
                switch (currentNode.getType().getKey()) {
                    case ScriptNode.NARRATIVE:
                    case ScriptNode.START:
                        System.out.println(currentNode.getContents().get(0).getFirst());
                        currentNode = scriptNodeHashMap.get(currentNode.getContents().get(0).getSecond());
                        break;
                    case ScriptNode.CHOICE:
                        for (int i = 0; i < currentNode.getContents().size(); i++) {
                            final String text = currentNode.getContents().get(i).getFirst();
                            System.out.println(i + " : " + text);
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        // Reading data using readLine
                        final Integer choice = Integer.valueOf(reader.readLine());
                        currentNode = scriptNodeHashMap.get(currentNode.getContents().get(choice).getSecond());
                        break;
                    case ScriptNode.END:
                        System.out.println(currentNode.getContents().get(0).getFirst());
                        run = false;
                        break;
                    default:
                        throw new Exception();
                }
            }
        } else {
            assertEquals(0, 1);
        }
    }
}
