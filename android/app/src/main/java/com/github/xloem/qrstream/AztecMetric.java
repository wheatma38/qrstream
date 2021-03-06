package com.github.xloem.qrstream;

public class AztecMetric extends CodeMetric {

    final private static int[] AZTEC_LAYERS_DIMENSION = {
            15,  19,  23,  27, // [0,1,2,3] = compact[1,2,3,4]
            // [4,...] = normal[4,...]
            31,  37,  41,  45,  49,
            53,  57,  61,  67,  71,  75,  79,  83,
            87,  91,  95,  101, 105, 109, 113, 117,
            121, 125, 131, 135, 139, 143, 147, 151
    };

    /*
    # AZTEC_LAYERS_CAPACITY generated with this rough python script

    from math import floor, ceil

    def getCapacity(layers, compact):
        if layers < 3:
            wordSize = 6
        elif layers < 9:
            wordSize = 8
        elif layers < 23:
            wordSize = 10
        else:
            wordSize = 12

        # total bits in aztec code
        if compact:
            capacity = 88
        else:
            capacity = 112
        capacity = (capacity + 16 * layers) * layers
        if compact and capacity > wordSize * 64:
            capacity = wordSize * 64
        else:
            capacity -= capacity % wordSize

        # brute force unstuff bits and remove ECC bits, zxing uses 33%
        for unstuffedBits in xrange(capacity):
            if wordSize * ceil(unstuffedBits * 1.0 / (wordSize - 1)) + floor(unstuffedBits * 33.0 / 100) + 11 > capacity:
                break
        capacity = unstuffedBits - 1

        # account for binary shift cruft
        bigshifts = capacity / (2078*8 + 21)
        remaining = capacity % (2078*8 + 21)
        # capacity is now in BYTES
        capacity = bigshifts * 2078
        if remaining >= (63*8 + 21):
            capacity += (remaining - 21) / 8
        elif remaining >= (32*8 + 20):
            capacity += min((remaining - 20) / 8, 62) # two extra small shifts
        else:
            capacity += min((remaining - 10) / 8, 31) # one extra small shift

        return capacity

    for a in range(33):
        if a < 4:
            layers = a + 1
            compact = True
        else:
            layers = a
            compact = False
        print getCapacity(layers, compact), ",",
     */

    final private static int[] AZTEC_LAYERS_CAPACITY = {
            6,    17,   31,   39,
            56,   77,   102,  129,  159,
            195 , 232 , 270 , 311 , 357 , 403 , 454 , 505 ,
            561 , 620 , 681 , 745 , 811 , 880 , 966 , 1043 ,
            1120, 1203, 1288, 1375, 1465, 1558, 1652, 1752
    };

    public static int aztecCapacity(int dimension) {

        // pick the most layers with an equal or smaller dimension
        int layersDimension, layers = AZTEC_LAYERS_DIMENSION.length;
        do {
            -- layers;
            layersDimension = AZTEC_LAYERS_DIMENSION[layers];
        } while (layersDimension > dimension && layers > 0);

        return AZTEC_LAYERS_CAPACITY[layers];
    }

    public int getMaxIndex() {
        return AZTEC_LAYERS_DIMENSION.length - 1;
    }

    public int getMinIndex() {
        // 0 through 3 represent compact layers 1 through 4
        return 0;
    }

    public void setIndex(int layers) {
        index = layers;
        dimension = AZTEC_LAYERS_DIMENSION[layers];
        capacity = AZTEC_LAYERS_CAPACITY[layers];
    }
}
