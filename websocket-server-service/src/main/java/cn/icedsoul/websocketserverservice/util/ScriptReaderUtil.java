package cn.icedsoul.websocketserverservice.util;

import cn.icedsoul.websocketserverservice.common.base.Tuple;
import cn.icedsoul.websocketserverservice.domain.dto.ScriptNode;
import reactor.util.function.Tuple2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 脚本读取工具类
 * @Author megumin
 */
public class ScriptReaderUtil {
    public static final String CHOICE_SEPARATER = "\\|";
    public static final String LINE_SEPARATER = "$";
    public static final String END_JUMP_TARGET = "1@3#5";

    public static HashMap<String, ScriptNode> parseScript(final String scriptPath) throws Exception {
        final BufferedReader in = new BufferedReader(new FileReader(scriptPath));
        HashMap<String, ScriptNode> parsedScript = new HashMap<>(16);
        String str;
        while ((str = in.readLine()) != null) {
            final Tuple<String, ScriptNode> scriptNodeTuple = parseLine(str);
            parsedScript.put(scriptNodeTuple.getFirst(), scriptNodeTuple.getSecond());
        }
        return parsedScript;
    }

    private static Tuple<String, ScriptNode> parseLine(final String line) throws Exception {
        if (line == null || line.length() == 0) {
            return null;
        }
        final List<String> lineSplited = Arrays.asList(line.split(CHOICE_SEPARATER));
        if(lineSplited.size() == 0) {
            throw new Exception("parse line failed: wrong format! line: " + line);
        }
        final String type = lineSplited.get(lineSplited.size() - 1);
        try {
            final String primaryKey = lineSplited.get(0);
            ScriptNode scriptNode;
            switch (type) {
                case ScriptNode.NARRATIVE:
                    //1|这是旁白|2|N|
                    scriptNode = ScriptNode.builder()
                            .type(ScriptNode.NodeType.NARRATIVE)
                            .contents(Collections.singletonList(new Tuple(lineSplited.get(1), lineSplited.get(2))))
                            .build();
                    return new Tuple<>(primaryKey, scriptNode);
                case ScriptNode.CHOICE:
                    //2|3|abc|3|def|4|xyz|5|C|
                    List<Tuple<String, String>> contents = new ArrayList<>();
                    for (int i = 0; i < Integer.valueOf(lineSplited.get(1)); i++) {
                        contents.add(new Tuple<>(lineSplited.get(2 * i + 2), lineSplited.get(2 * i + 3)));
                    }
                    scriptNode = ScriptNode.builder()
                            .type(ScriptNode.NodeType.CHOICE)
                            .contents(contents)
                            .build();
                    return new Tuple<>(primaryKey, scriptNode);
                case ScriptNode.START:
                    //1|游戏开始|2|S|
                    scriptNode = ScriptNode.builder()
                            .type(ScriptNode.NodeType.START)
                            .contents(Collections.singletonList(new Tuple(lineSplited.get(1), lineSplited.get(2))))
                            .build();
                    return new Tuple<>(primaryKey, scriptNode);
                case ScriptNode.END:
                    //8|game over3|E|
                    scriptNode = ScriptNode.builder()
                            .type(ScriptNode.NodeType.END)
                            .contents(Collections.singletonList(new Tuple(lineSplited.get(1), END_JUMP_TARGET)))
                            .build();
                    return new Tuple<>(primaryKey, scriptNode);
                default:
                    throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("parse line failed: invalid line type! line: " + line + ' ' + e.getMessage());
        }
    }
}
