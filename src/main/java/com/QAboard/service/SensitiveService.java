package com.QAboard.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    
    private static final String DEFAULT_REPLACEMENT = "**";
    
    
    private class TrieNode {
        private boolean end = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();
        
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }
        boolean isEnd(){
            return this.end;
        }
        void setEnd(boolean b) {
            this.end = b;
        }
        public int getSubNodeCount() {
            return subNodes.size();
        }
    }
    
    private TrieNode rootNode  = new TrieNode();
    
    private boolean isSymbol(char c) {
        return !CharUtils.isAsciiAlphanumeric(c);
    }
    public String filter(String text) {
       
        int start = 0, cur = 0;
        TrieNode root = rootNode;
       
        StringBuilder sb = new StringBuilder();
        
        while(cur < text.length()) {
            //System.out.println("Start " + start + "cur " + cur);
            char c = text.charAt(cur);
            if(isSymbol(c)) {
                if(root == rootNode) {
                    sb.append(c);
                    start++;
                }
                ++cur;
                continue;
            }
            if(root == null ) {
                break;
            }
            
            if(root.getSubNode(text.charAt(cur)) == null ||root.getSubNodeCount() == 0) {
                sb.append(text.charAt(start));
                start++;
                cur = start;
                root = rootNode;
            } else {
                if(root.isEnd()) {
                    sb.append("***");
                    System.out.println("************");
                    start = ++cur;
                    root = rootNode;
                } else {
                    root = root.getSubNode(text.charAt(cur));
                    cur++;
                }
            }

        }
        return sb.toString();
    }
 
    public void addSensitive(String word) {
        int i = 0;
        TrieNode root = rootNode;
        while(i < word.length()) {
            Character c = word.charAt(i);
            if (isSymbol(c)) {
                i++;
                continue;
            }
            if(!root.subNodes.containsKey(word.charAt(i))) {
                root.subNodes.put(word.charAt(i), new TrieNode());
            }
            root = root.subNodes.get(word.charAt(i));
            i++;
            if(i == word.length() - 1) {
                System.out.println("error");
                root.setEnd(true);
            }
        }
      //  rootNode  = root;
    }
    
    public void printAll() {
        printf(rootNode);
    }
    
    public void printf(TrieNode root) {
        for(Map.Entry<Character, TrieNode> entry: root.subNodes.entrySet()) {
            System.out.println(entry.getKey());
            if(root.end) System.out.print("true");
            else System.out.print("false");
            printf(root.subNodes.get(entry.getKey()));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addSensitive(lineTxt);
            }
            read.close();
        }catch(Exception e) {
            logger.error("Sensitive file fail");
        }
        
    }
    public static void main(String[] args) {
        SensitiveService test = new SensitiveService();
        test.addSensitive("donald trump");
        test.addSensitive("ac");
        test.addSensitive("ab");
        test.printAll();
        System.out.println(test.filter("abcbcddonald trump"));
    } 
    
            
}
