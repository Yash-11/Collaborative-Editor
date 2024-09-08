package com.example.editor.delta;

import java.util.List;

public class OpIterator {
    private List<Op> ops;
    private int index;
    private int offset;

    // Constructor
    public OpIterator(List<Op> ops) {
        this.ops = ops;
        this.index = 0;
        this.offset = 0;
    }

    // Check if there's a next operation
    public boolean hasNext() {
        return peekLength() < Integer.MAX_VALUE;
    }

    // Get the next operation with optional length
    public Op next(Integer length) {
        if (length == null) {
            length = Integer.MAX_VALUE;
        }
        Op nextOp = ops.get(index);
        if (nextOp != null) {
            int currentOffset = this.offset;
            int opLength = Op.length(nextOp);
            if (length >= opLength - currentOffset) {
                length = opLength - currentOffset;
                this.index += 1;
                this.offset = 0;
            } else {
                this.offset += length;
            }
            if (nextOp.getDelete() != null) {
                Op deleteOp = new Op();
                deleteOp.setDelete(length);
                return deleteOp;
            } else {
                Op retOp = new Op();
                if (nextOp.getAttributes() != null) {
                    retOp.setAttributes(nextOp.getAttributes());
                }
                if (nextOp.getRetain() instanceof Integer) {
                    retOp.setRetain(length);
                } else if (nextOp.getRetain() instanceof Object) {
                    retOp.setRetain(nextOp.getRetain()); // Assumes offset == 0, length == 1
                } else if (nextOp.getInsert() instanceof String) {
                    String insert = (String) nextOp.getInsert();
                    retOp.setInsert(insert.substring(currentOffset, currentOffset + length));
                } else {
                    retOp.setInsert(nextOp.getInsert()); // Assumes offset == 0, length == 1
                }
                return retOp;
            }
        } else {
            Op retainOp = new Op();
            retainOp.setRetain(Integer.MAX_VALUE);
            return retainOp;
        }
    }

    // Peek at the current operation
    public Op peek() {
        return ops.get(index);
    }

    // Get the length of the current operation
    public int peekLength() {
        if (ops.get(index) != null) {
            return Op.length(ops.get(index)) - offset;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    // Get the type of the current operation
    public String peekType() {
        Op op = ops.get(index);
        if (op != null) {
            if (op.getDelete() != null) {
                return "delete";
            } else if (op.getRetain() != null) {
                return "retain";
            } else {
                return "insert";
            }
        }
        return "retain";
    }

    // Get the rest of the operations
    public List<Op> rest() {
        if (!hasNext()) {
            return List.of();  // Empty list
        } else if (this.offset == 0) {
            return ops.subList(this.index, ops.size());
        } else {
            int currentOffset = this.offset;
            int currentIndex = this.index;
            Op next = this.next(null);
            List<Op> rest = ops.subList(this.index, ops.size());
            this.offset = currentOffset;
            this.index = currentIndex;
            rest.add(0, next);
            return rest;
        }
    }
}

