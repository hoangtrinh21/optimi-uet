package com.uet.hoangtrinh.nguoibanhang;

import com.uet.hoangtrinh.GaApplication;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.uet.hoangtrinh.GaApplication.MAP;

/**
 * @author: TrinhHoang - UET
 * @description: Thuật toán di truyền (Genetic Algorithm–GA) và các hàm sử dụng.
 */
public class Functions {
    /**
     * Tạo Ngẫu nhiên một mảng.
     *
     * @return Mảng các diểm ngẫu nhiên
     */
    private int[] randomContent() {
        int[] res = new int[GaApplication.amountNode];
        for (int i = 0; i < GaApplication.amountNode; i++) {
            res[i] = ThreadLocalRandom.current().nextInt(GaApplication.amountNode);
        }
        return res;
    }

    /**
     * Tạo ngẫu nhiên quần thẻ với số lượng cho trước (GaApplication.NUMBER_IN_GROUP).
     */
    public void createGroup() {
        Entity entityTmp;
        for (int i = 0; i < GaApplication.NUMBER_IN_GROUP; i++) {
            entityTmp = new Entity(randomContent());
            GaApplication.group.add(entityTmp);
        }
    }

    /**
     * Đánh giá các nghiệm trọng quần thể
     * sau đó sắp xếp các nghiệm theo điẻm đánh giá từ thấp đến cao.
     * Điểm đánh giá tính bằng tổng khoảng cách 2 điểm liền kề trong 1 nghiệm.
     */
    public void rated() {
        int scoreTmp;
        for (int i = 0; i < GaApplication.NUMBER_IN_GROUP; i++) {
            scoreTmp = 0; // Điểm đánh giá của nghiệm thứ i
            // Tính điểm đánh giá
            for (int j = 0; j < GaApplication.amountNode - 1; j++) {
                scoreTmp += MAP[GaApplication.group.get(i).getContent()[j]]
                                                [GaApplication.group.get(i).getContent()[j + 1]];
            }
            // TH nghiệm có 2 điểm trùng nhau có điểm đánh giá cộng thêm x vào điểm đánh giá
            // x = Tổng số điểm trong bài toán * khoảng cách lớn nhất giữa 2 điểm * 2
            for(int j = 0; j < GaApplication.amountNode - 1; j++) {
                for (int t = j + 1; t < GaApplication.amountNode; t++)
                    if (GaApplication.group.get(i).getContent()[j] == GaApplication.group.get(i).getContent()[t] &&
                        t != j + 1) {
                        scoreTmp += GaApplication.amountNode * GaApplication.distanceMax * 2;
                    }
            }
            GaApplication.group.get(i).setScore(scoreTmp); // Gán điểm đánh giá cho nghiệm thứ i
        }
        sortGroup(GaApplication.group); // Sắp xếp danh sách nghiệm theo điểm đánh giá thấp -> cao
    }

    /**
     * Chọn các nghiệm có điểm đánh giá thấp,
     * nhân bản các nghiệm đó tạo ra quần thể có với số nghiệm bằng với ban đầu
     */
    public void selected() {
        int thresholeIndex = (int) (GaApplication.NUMBER_IN_GROUP * 50.0/100);
        for (int i = thresholeIndex; i < GaApplication.NUMBER_IN_GROUP; i++) { // Nhân bản nghiệm
                GaApplication.group.set(i,
                        new Entity(GaApplication.group
                                .get(ThreadLocalRandom.current()
                                        .nextInt(thresholeIndex / 2 + thresholeIndex / 3))));
        }
        sortGroup(GaApplication.group); // Sắp xếp danh sách nghiệm theo điểm đánh giá thấp -> cao
    }

    /**
     * Lai ghép hai nghiệm,
     * nhân bản các nghiệm đó tạo ra quần thể có với số nghiệm bằng với ban đầu
     */
    public void crossover() {
        for (int i = 0; i < GaApplication.NUMBER_IN_GROUP * 25.0/100; i++){
            int father = ThreadLocalRandom.current().nextInt(GaApplication.NUMBER_IN_GROUP);
            int mother = ThreadLocalRandom.current().nextInt(GaApplication.NUMBER_IN_GROUP);
            for (int j = 0; j < GaApplication.amountNode; j++) // Lai ghép
                if (ThreadLocalRandom.current().nextInt(2) == 1) {
                    int temp = GaApplication.group.get(father).getContent()[j];
                    GaApplication.group.get(father).getContent()[j] = GaApplication.group.get(mother).getContent()[j];
                    GaApplication.group.get(mother).getContent()[j] = temp;
                }
        }
        sortGroup(GaApplication.group); // Sắp xếp danh sách nghiệm theo điểm đánh giá thấp -> cao
    }

    /**
     * Biến dị các nghiệm
     */
    public void mutation() {
        for (int i = 0; i < GaApplication.NUMBER_IN_GROUP * 0.25; i++) {
            int index = ThreadLocalRandom.current().nextInt(GaApplication.NUMBER_IN_GROUP);
            int bit = ThreadLocalRandom.current().nextInt(GaApplication.amountNode);
            GaApplication.group.get(index).getContent()[bit] = // Đột biến nghiệm
                    ThreadLocalRandom.current().nextInt(GaApplication.amountNode);
        }
        sortGroup(GaApplication.group); // Sắp xếp danh sách nghiệm theo điểm đánh giá thấp -> cao
    }

    public int sccoreRepeat(int scorePre, int score, int count) {
        return scorePre == score ? ++count : 1;
    }

    /**
     * Kiểm tra đồ thị có liên thông không. Nghiệm đầu tiên có tạo thành đồ thị liên thông không
     * @return true khi nghiệm đầu tiên tạo thành đồ thị liên thông, false khi ngược lại
     */
    public boolean isConnectedGraph() {
        return GaApplication.group.get(0).getScore() <= 5000;
    }

    /**
     * Giảm số lượng nghiệm trong quần thể
     * @param i số lần đã lặp
     * @param threshold ngưỡng
     * @param countReduce số lần đã giảm
     * @param count số lần điểm đánh giá lặp lại liên tiếp
     */
    public void reduceGroup(int i, int threshold, int countReduce, int count) {
        if ((i == threshold || i == threshold / 2 || count == threshold ||
                i == threshold * 2 || count == threshold / 2) && countReduce < 4) {
            GaApplication.group =
                    GaApplication.group.stream()
                            .limit((long) (GaApplication.NUMBER_IN_GROUP * 0.8))
                            .collect(Collectors.toList());
            GaApplication.NUMBER_IN_GROUP = (int) (GaApplication.NUMBER_IN_GROUP * 0.8);
            countReduce++;
        }
    }

    /**
     * Sắp xếp danh sách theo điểm đánh giá thấp -> cao.
     * @param group Danh sách cần sắp xếp
     */
    private void sortGroup(List<Entity> group) {
        group.sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()) * -1);
    }
}
