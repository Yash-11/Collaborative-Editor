package com.example.editor.delta;

import java.util.ArrayList;
import java.util.List;

import com.example.editor.delta.Op;

import java.util.*;

public class Delta {
    private List<Op> ops;

    public Delta(List<Op> ops) {
        this.ops = new ArrayList<>(ops);
    }

    public void push(Op op) {
        this.ops.add(op);
    }

    public Delta concat(Delta other) {
        List<Op> combinedOps = new ArrayList<>(this.ops);
        combinedOps.addAll(other.ops);
        return new Delta(combinedOps);
    }

    public Delta chop() {
        List<Op> choppedOps = new ArrayList<>(this.ops);
        Op lastOp = choppedOps.isEmpty() ? null : choppedOps.get(choppedOps.size() - 1);
        if (lastOp != null && lastOp.getRetain() != null && lastOp.getAttributes() == null) {
            choppedOps.remove(choppedOps.size() - 1);
        }
        return new Delta(choppedOps);
    }

    // public static Delta compose(Delta thisDelta, Delta otherDelta) {
    //     OpIterator thisIter = new OpIterator(thisDelta.ops);
    //     OpIterator otherIter = new OpIterator(otherDelta.ops);
    //     List<Op> ops = new ArrayList<>();

    //     Op firstOther = otherIter.peek();
    //     if (firstOther != null && firstOther.getRetain() != null && firstOther.getAttributes() == null) {
    //         int firstLeft = firstOther.getRetain();
    //         while (thisIter.peekType().equals("insert") && thisIter.peekLength() <= firstLeft) {
    //             firstLeft -= thisIter.peekLength();
    //             ops.add(thisIter.next());
    //         }
    //         if (firstOther.getRetain() - firstLeft > 0) {
    //             otherIter.next(firstOther.getRetain() - firstLeft);
    //         }
    //     }

    //     Delta delta = new Delta(ops);

    //     while (thisIter.hasNext() || otherIter.hasNext()) {
    //         if (otherIter.peekType().equals("insert")) {
    //             delta.push(otherIter.next());
    //         } else if (thisIter.peekType().equals("delete")) {
    //             delta.push(thisIter.next());
    //         } else {
    //             int length = Math.min(thisIter.peekLength(), otherIter.peekLength());
    //             Op thisOp = thisIter.next(length);
    //             Op otherOp = otherIter.next(length);
    //             Op newOp = new Op();

    //             if (otherOp.getRetain() != null) {
    //                 if (thisOp.getRetain() != null) {
    //                     newOp.setRetain(
    //                         otherOp.getRetain() instanceof Number
    //                             ? length
    //                             : otherOp.getRetain()
    //                     );
    //                 } else {
    //                     if (otherOp.getRetain() instanceof Number) {
    //                         if (thisOp.getRetain() == null) {
    //                             newOp.setInsert(thisOp.getInsert());
    //                         } else {
    //                             newOp.setRetain(thisOp.getRetain());
    //                         }
    //                     } else {
    //                         // String action = thisOp.getRetain() == null ? "insert" : "retain";
    //                         // Object[] embedData = getEmbedTypeAndData(thisOp.get(action), otherOp.getRetain());
    //                         // String embedType = (String) embedData[0];
    //                         // Object thisData = embedData[1];
    //                         // Object otherData = embedData[2];
    //                         // Handler handler = Delta.getHandler(embedType);
    //                         // newOp.set(action, Map.of(embedType, handler.compose(thisData, otherData, action.equals("retain"))));
    //                     }
    //                 }
    //                 Map<String, Object> attributes = AttributeMap.compose(
    //                     thisOp.getAttributes(), otherOp.getAttributes(), thisOp.getRetain() != null
    //                 );
    //                 if (attributes != null) {
    //                     newOp.setAttributes(attributes);
    //                 }
    //                 delta.push(newOp);

    //                 if (!otherIter.hasNext() && isEqual(delta.ops.get(delta.ops.size() - 1), newOp)) {
    //                     Delta rest = new Delta(thisIter.rest());
    //                     return delta.concat(rest).chop();
    //                 }
    //             } else if (otherOp.getDelete() != null &&
    //                     (thisOp.getRetain() != null || (thisOp.getRetain() instanceof Map && thisOp.getRetain() != null))) {
    //                 delta.push(otherOp);
    //             }
    //         }
    //     }
    //     return delta.chop();
    // }

    // Example methods that might be used in the class:
    // private static Object[] getEmbedTypeAndData(Object thisOpData, Object otherOpData) {
    //     // Implementation for extracting embedType, thisData, otherData
    //     return new Object[] {};
    // }

    // private static Handler getHandler(String embedType) {
    //     // Implementation for getting the appropriate Handler for the embedType
    //     return new Handler();
    // }

    private static boolean isEqual(Op op1, Op op2) {
        // Implementation for comparing two Op objects
        return false;
    }

    
}

