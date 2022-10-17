package com.uet.guava;

import com.google.common.graph.*;
import com.uet.hoangtrinh.GaApplication;
import com.uet.hoangtrinh.nguoibanhang.Entity;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CreateArray {
    /**
     * Tạo đồ thị ngẫu nhiên
     * @return
     */
    public static MutableValueGraph<Integer, Integer> createGraph() {
        MutableValueGraph<Integer, Integer> graph = ValueGraphBuilder.undirected().build();

        int node1 = 1, node2 = 2, edge = 3;
        graph.putEdgeValue(node1, node2, edge);
        Random random = new Random();
        for (int i = 0; i < 5000; i++) {
            node1 = random.nextInt(GaApplication.amountNode);
            do {
                node2 = random.nextInt(GaApplication.amountNode);
            } while (node1 == node2);
            edge = random.nextInt(GaApplication.distanceMax) + 1;
            graph.putEdgeValue(node1, node2, edge);
        }
        return graph;
    }

    /**
     * Tạo mảng ngẫu nhiên
     * @param graph
     * @return
     */
    public static int[][] run(MutableValueGraph<Integer, Integer> graph) {
        int maxValue = GaApplication.amountNode * GaApplication.distanceMax * 2;

        List<Integer> listNode = graph.nodes().stream().collect(Collectors.toList());
        int[][] g = new int[GaApplication.amountNode][GaApplication.amountNode];
        for (int i = 0; i < GaApplication.amountNode; i++) {
            for (int j = 0; j < GaApplication.amountNode; j++) {
                if (i == j || !graph.hasEdgeConnecting(listNode.get(i), listNode.get(j))) g[i][j] = maxValue;

                if (graph.hasEdgeConnecting(listNode.get(i), listNode.get(j))) {
                    g[i][j] = graph.edgeValue(listNode.get(i), listNode.get(j)).get();
                }
                System.out.printf("%4d|",g[i][j]);
            }
            System.out.println();
        }
        return g;
    }


    public static  void check(Entity entity, int[][] map) {
        int tmp = 0;
        for (int i = 0; i < 49; i++) {
            tmp += (map[entity.getContent()[i]][entity.getContent()[i+1]]);
        }

        for(int j = 0; j < GaApplication.amountNode - 1; j++) {
            for (int k = j + 1; k < GaApplication.amountNode; k++)
                if (entity.getContent()[j] == entity.getContent()[k] && k != j + 1) {
                    tmp += GaApplication.amountNode * GaApplication.distanceMax * 2;
                }
        }
        System.out.println(tmp);
    }
}
