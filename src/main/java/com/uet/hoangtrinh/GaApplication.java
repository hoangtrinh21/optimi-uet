package com.uet.hoangtrinh;

import com.google.common.graph.MutableValueGraph;
import com.uet.guava.CreateArray;
import com.uet.hoangtrinh.nguoibanhang.Entity;
import com.uet.hoangtrinh.nguoibanhang.Functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: TrinhHoang - UET
 * @description: Ứng dụng thuật toán di truyền vào giải bài toán "Người bán hàng"
 * Bài toán: Cho trước một danh sách các thành phố và khoảng cách giữa chúng,
 *           tìm chu trình ngắn nhất thăm mỗi thành phố đúng một lần
 * Ý tưởng: Áp dụng thuật toán di truyền.
 * Cài đặt:
 *      Khởi tạo ngẫu nhiên danh sách các nghiệm
 *      while ()
 *          1.Đánh giá: Đánh giá độ thích nghi của nghiệm theo điểm đánh giá
 *          2.Kiểm tra: Kiểm tra điều kiện kết thúc
 *          3.Chọn lọc: Chọn các nghiệm có điểm đánh giá thấp
 *          4.Lai ghép: Lai ghép các hai nghiệm
 *          5.Đột biến: Đột biến nghiệm
 */
public class GaApplication {
    public static final int     distanceMax     = 10; // Khoảng cách lớn nhất giữa 2 node (thành phố)
    public static final int     amountNode      = 50; // Tổng số điểm cần đi qua
    public static final MutableValueGraph<Integer, Integer> GRAPH = CreateArray.createGraph();
    public static final int     MAP[][]         = // Ma trận 2 chiều thể hiện khoảng cách giữa 2 thành phố
            CreateArray.run(GRAPH);
    public static int     NUMBER_IN_GROUP = 10000; // Số nghiệm trong quần thể
    public static List<Entity>  group           = new ArrayList<>(); // Danh sách các nghiệm trong quần thể

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
        int scorePre = 0; // Điểm đánh giá của nghiệm tốt nhất trong quần thể trước
        int score = 0; // Điểm của nghiệm có nghiệm tốt nhất trong quần thể hiện tại
        int count = 1; // Số lần lặp lại liên tiếp điểm
        int threshole = 1000;
        while (true) {
//            System.out.println("Running...");
            functions.rated(); // danh gia
            // start: điều kiện dừng
//            if (i == 1) break;
            score = group.get(0).getScore();
            count = functions.sccoreRepeat(scorePre, score, count);

            // Giảm số lượng nghiệm trong quần thể
            if ((count > 500
                && (group.get(0).getScore() == 170 || group.get(0).getScore() == 150 ||
                    group.get(0).getScore() == 140 || group.get(0).getScore() == 130 ||
                    group.get(0).getScore() == 120 || group.get(0).getScore() == 110 ||
                    group.get(0).getScore() == 100 || group.get(0).getScore() == 95 ||
                    group.get(0).getScore() == 90 || group.get(0).getScore() == 80 ||
//                    group.get(0).getScore() == 100 || group.get(0).getScore() == 90 ||
                    group.get(0).getScore() == 85 || group.get(0).getScore() == 75))
                    ||
                    count > 5000
                    ) {
                group = group.stream().limit((long) (NUMBER_IN_GROUP * 0.8)).collect(Collectors.toList());
                NUMBER_IN_GROUP = (int) (NUMBER_IN_GROUP * 0.8);
            }

            // start debug
            System.out.println(i + "-" + group.get(0).getScore() + ": " + count);
//            if (i == 100) break;
            // end debug

            if ((i > 1000000 && group.get(0).getScore() < 100 &&
                    (functions.canStop() ||
                            (!functions.canStop() && (count > threshole))))
                || group.size() < 10) {
                break;
            }
            scorePre = score;

            // end: điều kiện dừng
            functions.selected(); // chon loc
            functions.crossover(); // di truyen
            functions.mutation(); // bien di
            i++;
        }
        // end: GA
        // print nghiem
        System.out.println(group.size());
        System.out.println(group.get(0).getScore()  + ": " + Arrays.toString(group.get(0).getContent()));
        CreateArray.check(group.get(0), MAP);
    }
}
