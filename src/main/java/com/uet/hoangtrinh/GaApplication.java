package com.uet.hoangtrinh;

import com.google.common.graph.MutableValueGraph;
import com.uet.guava.CreateArray;
import com.uet.hoangtrinh.nguoibanhang.Entity;
import com.uet.hoangtrinh.nguoibanhang.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: TrinhHoang - UET
 * @description: Ứng dụng thuật toán di truyền vào giải bài toán "Người bán hàng"
 * Bài toán: Cho trước một danh sách các thành phố và khoảng cách giữa chúng,
 *           tìm chu trình ngắn nhất thăm mỗi thành phố đúng một lần
 */
public class GaApplication {
    public static final int     distanceMax     = 10; // khoang cach lon nhat giua 2 thanh pho
    public static final int     amountNode      = 50; // tong so thanh pho
    public static final MutableValueGraph<Integer, Integer> GRAPH = CreateArray.createGraph();
    public static final int     MAP[][]         = CreateArray.run(GRAPH); // khoi tao ma tran 2 chieu chieu the hien khoang cach giua 2 thanh pho
    public static final int     NUMBER_IN_GROUP = 10000; // so phan tu trong quan the
    public static List<Entity>  group           = new ArrayList<>(); // danh sach phan tu trong quan the

    public static void main(String[] args) {
        run();
    }

    /**
     * Run application
     */
    public static void run() {
        Functions functions = new Functions();
        // start: GA
        functions.createGroup(); // khoi tao quan the ngau nhien
        int i = 0;
        while (true) {
            System.out.println("Running...");
            functions.rating(); // danh gia
            // start: điều kiện dừng
//            if (i == 1) break;
            if (i > 5000 && functions.canStop()) {
                break;
            }
            // end: điều kiện dừng
            functions.selected(); // chon loc
            functions.crossover(); // di truyen
            functions.mutation(); // bien di
            i++;
        }
        // end: GA
        // print nghiem
        System.out.println(group.get(0).getScore()  + ": " + Arrays.toString(group.get(0).getContent()));
        CreateArray.check(group.get(0), MAP);
    }
}
