(ns net.cgrand.regex.unicode
  (:require [net.cgrand.regex.charset :as cs]
            [clojure.java.io :as io]
            [clojure.pprint :as p]))

(defn- charset-expr [cs]
  (let [rs (cs/ranges (cs/charset cs))
        single-char? (fn [[a b]] (and a (= a b)))
        m (reduce conj (sorted-map-by (fn [a b]
                                        (or (nil? a) (and b (< a b)))))
                  (remove single-char? rs))
        s (reduce #(conj %1 (first %2)) (sorted-set) (filter single-char? rs))]
    (cond
      (and (seq m) (seq s)) (list `cs/+ m s)
      (seq m) (list `cs/+ m)
      :else (list `cs/+ s))))

#_(with-open [r (io/reader (io/as-url "http://www.unicode.org/Public/UNIDATA/UnicodeData.txt"))]
  (p/with-pprint-dispatch p/code-dispatch 
    (p/pprint
      (list 'def 'cats
            (into (sorted-map)
                  (for [[cat cs]
                        (reduce #(merge-with cs/+ %1 %2)
                                (for [^String line (line-seq r)
                                      :let [[n _ cat] (.split line ";")
                                            n (Integer/parseInt n 16)]
                                      :when (<= 0 n 0xFFFF)]
                                  {cat (cs/charset n)}))]
                    [cat (charset-expr cs)]))))))

;; Unicode categories, straight from the source.

(def cats
 {"Cc" (net.cgrand.regex.charset/+ {0 31, 127 159}),
  "Cf"
  (net.cgrand.regex.charset/+
    {1536 1540,
     8203 8207,
     8234 8238,
     8288 8292,
     8298 8303,
     65529 65531}
    #{173 1757 1807 65279}),
  "Co" (net.cgrand.regex.charset/+ #{57344 63743}),
  "Cs"
  (net.cgrand.regex.charset/+
    {56191 56192, 56319 56320}
    #{55296 57343}),
  "Ll"
  (net.cgrand.regex.charset/+
    {97 122,
     223 246,
     248 255,
     311 312,
     328 329,
     382 384,
     396 397,
     409 411,
     426 427,
     441 442,
     445 447,
     476 477,
     495 496,
     563 569,
     575 576,
     591 659,
     661 687,
     891 893,
     940 974,
     976 977,
     981 983,
     1007 1011,
     1019 1020,
     1072 1119,
     1230 1231,
     1377 1415,
     7424 7467,
     7531 7543,
     7545 7578,
     7829 7837,
     7935 7943,
     7952 7957,
     7968 7975,
     7984 7991,
     8000 8005,
     8016 8023,
     8032 8039,
     8048 8061,
     8064 8071,
     8080 8087,
     8096 8103,
     8112 8116,
     8118 8119,
     8130 8132,
     8134 8135,
     8144 8147,
     8150 8151,
     8160 8167,
     8178 8180,
     8182 8183,
     8462 8463,
     8508 8509,
     8518 8521,
     11312 11358,
     11365 11366,
     11379 11380,
     11382 11387,
     11491 11492,
     11520 11557,
     42799 42801,
     42865 42872,
     64256 64262,
     64275 64279,
     65345 65370}
    #{181 257 259 261 263 265 267 269 271 273 275 277 279 281 283 285
      287 289 291 293 295 297 299 301 303 305 307 309 314 316 318 320
      322 324 326 331 333 335 337 339 341 343 345 347 349 351 353 355
      357 359 361 363 365 367 369 371 373 375 378 380 387 389 392 402
      405 414 417 419 421 424 429 432 436 438 454 457 460 462 464 466
      468 470 472 474 479 481 483 485 487 489 491 493 499 501 505 507
      509 511 513 515 517 519 521 523 525 527 529 531 533 535 537 539
      541 543 545 547 549 551 553 555 557 559 561 572 578 583 585 587
      589 881 883 887 912 985 987 989 991 993 995 997 999 1001 1003
      1005 1013 1016 1121 1123 1125 1127 1129 1131 1133 1135 1137 1139
      1141 1143 1145 1147 1149 1151 1153 1163 1165 1167 1169 1171 1173
      1175 1177 1179 1181 1183 1185 1187 1189 1191 1193 1195 1197 1199
      1201 1203 1205 1207 1209 1211 1213 1215 1218 1220 1222 1224 1226
      1228 1233 1235 1237 1239 1241 1243 1245 1247 1249 1251 1253 1255
      1257 1259 1261 1263 1265 1267 1269 1271 1273 1275 1277 1279 1281
      1283 1285 1287 1289 1291 1293 1295 1297 1299 1301 1303 1305 1307
      1309 1311 1313 1315 1317 1319 7681 7683 7685 7687 7689 7691 7693
      7695 7697 7699 7701 7703 7705 7707 7709 7711 7713 7715 7717 7719
      7721 7723 7725 7727 7729 7731 7733 7735 7737 7739 7741 7743 7745
      7747 7749 7751 7753 7755 7757 7759 7761 7763 7765 7767 7769 7771
      7773 7775 7777 7779 7781 7783 7785 7787 7789 7791 7793 7795 7797
      7799 7801 7803 7805 7807 7809 7811 7813 7815 7817 7819 7821 7823
      7825 7827 7839 7841 7843 7845 7847 7849 7851 7853 7855 7857 7859
      7861 7863 7865 7867 7869 7871 7873 7875 7877 7879 7881 7883 7885
      7887 7889 7891 7893 7895 7897 7899 7901 7903 7905 7907 7909 7911
      7913 7915 7917 7919 7921 7923 7925 7927 7929 7931 7933 8126 8458
      8467 8495 8500 8505 8526 8580 11361 11368 11370 11372 11377 11393
      11395 11397 11399 11401 11403 11405 11407 11409 11411 11413 11415
      11417 11419 11421 11423 11425 11427 11429 11431 11433 11435 11437
      11439 11441 11443 11445 11447 11449 11451 11453 11455 11457 11459
      11461 11463 11465 11467 11469 11471 11473 11475 11477 11479 11481
      11483 11485 11487 11489 11500 11502 11507 11559 11565 42561 42563
      42565 42567 42569 42571 42573 42575 42577 42579 42581 42583 42585
      42587 42589 42591 42593 42595 42597 42599 42601 42603 42605 42625
      42627 42629 42631 42633 42635 42637 42639 42641 42643 42645 42647
      42787 42789 42791 42793 42795 42797 42803 42805 42807 42809 42811
      42813 42815 42817 42819 42821 42823 42825 42827 42829 42831 42833
      42835 42837 42839 42841 42843 42845 42847 42849 42851 42853 42855
      42857 42859 42861 42863 42874 42876 42879 42881 42883 42885 42887
      42892 42894 42897 42899 42913 42915 42917 42919 42921 43002}),
  "Lm"
  (net.cgrand.regex.charset/+
    {688 705,
     710 721,
     736 740,
     1765 1766,
     2036 2037,
     7288 7293,
     7468 7530,
     7579 7615,
     8336 8348,
     11388 11389,
     12337 12341,
     12445 12446,
     12540 12542,
     42232 42237,
     42775 42783,
     43000 43001,
     43763 43764,
     65438 65439}
    #{748 750 884 890 1369 1600 2042 2074 2084 2088 2417 3654 3782 4348
      6103 6211 6823 7544 8305 8319 11631 11823 12293 12347 40981 42508
      42623 42864 42888 43471 43632 43741 65392}),
  "Lo"
  (net.cgrand.regex.charset/+
    {448 451,
     1488 1514,
     1520 1522,
     1568 1599,
     1601 1610,
     1646 1647,
     1649 1747,
     1774 1775,
     1786 1788,
     1810 1839,
     1869 1957,
     1994 2026,
     2048 2069,
     2112 2136,
     2210 2220,
     2308 2361,
     2392 2401,
     2418 2423,
     2425 2431,
     2437 2444,
     2447 2448,
     2451 2472,
     2474 2480,
     2486 2489,
     2524 2525,
     2527 2529,
     2544 2545,
     2565 2570,
     2575 2576,
     2579 2600,
     2602 2608,
     2610 2611,
     2613 2614,
     2616 2617,
     2649 2652,
     2674 2676,
     2693 2701,
     2703 2705,
     2707 2728,
     2730 2736,
     2738 2739,
     2741 2745,
     2784 2785,
     2821 2828,
     2831 2832,
     2835 2856,
     2858 2864,
     2866 2867,
     2869 2873,
     2908 2909,
     2911 2913,
     2949 2954,
     2958 2960,
     2962 2965,
     2969 2970,
     2974 2975,
     2979 2980,
     2984 2986,
     2990 3001,
     3077 3084,
     3086 3088,
     3090 3112,
     3114 3123,
     3125 3129,
     3160 3161,
     3168 3169,
     3205 3212,
     3214 3216,
     3218 3240,
     3242 3251,
     3253 3257,
     3296 3297,
     3313 3314,
     3333 3340,
     3342 3344,
     3346 3386,
     3424 3425,
     3450 3455,
     3461 3478,
     3482 3505,
     3507 3515,
     3520 3526,
     3585 3632,
     3634 3635,
     3648 3653,
     3713 3714,
     3719 3720,
     3732 3735,
     3737 3743,
     3745 3747,
     3754 3755,
     3757 3760,
     3762 3763,
     3776 3780,
     3804 3807,
     3904 3911,
     3913 3948,
     3976 3980,
     4096 4138,
     4176 4181,
     4186 4189,
     4197 4198,
     4206 4208,
     4213 4225,
     4304 4346,
     4349 4680,
     4682 4685,
     4688 4694,
     4698 4701,
     4704 4744,
     4746 4749,
     4752 4784,
     4786 4789,
     4792 4798,
     4802 4805,
     4808 4822,
     4824 4880,
     4882 4885,
     4888 4954,
     4992 5007,
     5024 5108,
     5121 5740,
     5743 5759,
     5761 5786,
     5792 5866,
     5888 5900,
     5902 5905,
     5920 5937,
     5952 5969,
     5984 5996,
     5998 6000,
     6016 6067,
     6176 6210,
     6212 6263,
     6272 6312,
     6320 6389,
     6400 6428,
     6480 6509,
     6512 6516,
     6528 6571,
     6593 6599,
     6656 6678,
     6688 6740,
     6917 6963,
     6981 6987,
     7043 7072,
     7086 7087,
     7098 7141,
     7168 7203,
     7245 7247,
     7258 7287,
     7401 7404,
     7406 7409,
     7413 7414,
     8501 8504,
     11568 11623,
     11648 11670,
     11680 11686,
     11688 11694,
     11696 11702,
     11704 11710,
     11712 11718,
     11720 11726,
     11728 11734,
     11736 11742,
     12353 12438,
     12449 12538,
     12549 12589,
     12593 12686,
     12704 12730,
     12784 12799,
     40960 40980,
     40982 42124,
     42192 42231,
     42240 42507,
     42512 42527,
     42538 42539,
     42656 42725,
     43003 43009,
     43011 43013,
     43015 43018,
     43020 43042,
     43072 43123,
     43138 43187,
     43250 43255,
     43274 43301,
     43312 43334,
     43360 43388,
     43396 43442,
     43520 43560,
     43584 43586,
     43588 43595,
     43616 43631,
     43633 43638,
     43648 43695,
     43701 43702,
     43705 43709,
     43739 43740,
     43744 43754,
     43777 43782,
     43785 43790,
     43793 43798,
     43808 43814,
     43816 43822,
     43968 44002,
     55216 55238,
     55243 55291,
     63744 64109,
     64112 64217,
     64287 64296,
     64298 64310,
     64312 64316,
     64320 64321,
     64323 64324,
     64326 64433,
     64467 64829,
     64848 64911,
     64914 64967,
     65008 65019,
     65136 65140,
     65142 65276,
     65382 65391,
     65393 65437,
     65440 65470,
     65474 65479,
     65482 65487,
     65490 65495,
     65498 65500}
    #{170 186 443 660 1749 1791 1808 1969 2208 2365 2384 2482 2493 2510
      2654 2749 2768 2877 2929 2947 2972 3024 3133 3261 3294 3389 3406
      3517 3716 3722 3725 3749 3751 3773 3840 4159 4193 4238 4696 4800
      6108 6314 12294 12348 12447 12543 13312 19893 19968 40908 42606
      43259 43642 43697 43712 43714 43762 44032 55203 64285 64318}),
  "Lt"
  (net.cgrand.regex.charset/+
    {8072 8079, 8088 8095, 8104 8111}
    #{453 456 459 498 8124 8140 8188}),
  "Lu"
  (net.cgrand.regex.charset/+
    {65 90,
     192 214,
     216 222,
     376 377,
     385 386,
     390 391,
     393 395,
     398 401,
     403 404,
     406 408,
     412 413,
     415 416,
     422 423,
     430 431,
     433 435,
     439 440,
     502 504,
     570 571,
     573 574,
     579 582,
     904 906,
     910 911,
     913 929,
     931 939,
     978 980,
     1017 1018,
     1021 1071,
     1216 1217,
     1329 1366,
     4256 4293,
     7944 7951,
     7960 7965,
     7976 7983,
     7992 7999,
     8008 8013,
     8040 8047,
     8120 8123,
     8136 8139,
     8152 8155,
     8168 8172,
     8184 8187,
     8459 8461,
     8464 8466,
     8473 8477,
     8490 8493,
     8496 8499,
     8510 8511,
     11264 11310,
     11362 11364,
     11373 11376,
     11390 11392,
     42877 42878,
     65313 65338}
    #{256 258 260 262 264 266 268 270 272 274 276 278 280 282 284 286
      288 290 292 294 296 298 300 302 304 306 308 310 313 315 317 319
      321 323 325 327 330 332 334 336 338 340 342 344 346 348 350 352
      354 356 358 360 362 364 366 368 370 372 374 379 381 388 418 420
      425 428 437 444 452 455 458 461 463 465 467 469 471 473 475 478
      480 482 484 486 488 490 492 494 497 500 506 508 510 512 514 516
      518 520 522 524 526 528 530 532 534 536 538 540 542 544 546 548
      550 552 554 556 558 560 562 577 584 586 588 590 880 882 886 902
      908 975 984 986 988 990 992 994 996 998 1000 1002 1004 1006 1012
      1015 1120 1122 1124 1126 1128 1130 1132 1134 1136 1138 1140 1142
      1144 1146 1148 1150 1152 1162 1164 1166 1168 1170 1172 1174 1176
      1178 1180 1182 1184 1186 1188 1190 1192 1194 1196 1198 1200 1202
      1204 1206 1208 1210 1212 1214 1219 1221 1223 1225 1227 1229 1232
      1234 1236 1238 1240 1242 1244 1246 1248 1250 1252 1254 1256 1258
      1260 1262 1264 1266 1268 1270 1272 1274 1276 1278 1280 1282 1284
      1286 1288 1290 1292 1294 1296 1298 1300 1302 1304 1306 1308 1310
      1312 1314 1316 1318 4295 4301 7680 7682 7684 7686 7688 7690 7692
      7694 7696 7698 7700 7702 7704 7706 7708 7710 7712 7714 7716 7718
      7720 7722 7724 7726 7728 7730 7732 7734 7736 7738 7740 7742 7744
      7746 7748 7750 7752 7754 7756 7758 7760 7762 7764 7766 7768 7770
      7772 7774 7776 7778 7780 7782 7784 7786 7788 7790 7792 7794 7796
      7798 7800 7802 7804 7806 7808 7810 7812 7814 7816 7818 7820 7822
      7824 7826 7828 7838 7840 7842 7844 7846 7848 7850 7852 7854 7856
      7858 7860 7862 7864 7866 7868 7870 7872 7874 7876 7878 7880 7882
      7884 7886 7888 7890 7892 7894 7896 7898 7900 7902 7904 7906 7908
      7910 7912 7914 7916 7918 7920 7922 7924 7926 7928 7930 7932 7934
      8025 8027 8029 8031 8450 8455 8469 8484 8486 8488 8517 8579 11360
      11367 11369 11371 11378 11381 11394 11396 11398 11400 11402 11404
      11406 11408 11410 11412 11414 11416 11418 11420 11422 11424 11426
      11428 11430 11432 11434 11436 11438 11440 11442 11444 11446 11448
      11450 11452 11454 11456 11458 11460 11462 11464 11466 11468 11470
      11472 11474 11476 11478 11480 11482 11484 11486 11488 11490 11499
      11501 11506 42560 42562 42564 42566 42568 42570 42572 42574 42576
      42578 42580 42582 42584 42586 42588 42590 42592 42594 42596 42598
      42600 42602 42604 42624 42626 42628 42630 42632 42634 42636 42638
      42640 42642 42644 42646 42786 42788 42790 42792 42794 42796 42798
      42802 42804 42806 42808 42810 42812 42814 42816 42818 42820 42822
      42824 42826 42828 42830 42832 42834 42836 42838 42840 42842 42844
      42846 42848 42850 42852 42854 42856 42858 42860 42862 42873 42875
      42880 42882 42884 42886 42891 42893 42896 42898 42912 42914 42916
      42918 42920 42922}),
  "Mc"
  (net.cgrand.regex.charset/+
    {2366 2368,
     2377 2380,
     2382 2383,
     2434 2435,
     2494 2496,
     2503 2504,
     2507 2508,
     2622 2624,
     2750 2752,
     2763 2764,
     2818 2819,
     2887 2888,
     2891 2892,
     3006 3007,
     3009 3010,
     3014 3016,
     3018 3020,
     3073 3075,
     3137 3140,
     3202 3203,
     3264 3268,
     3271 3272,
     3274 3275,
     3285 3286,
     3330 3331,
     3390 3392,
     3398 3400,
     3402 3404,
     3458 3459,
     3535 3537,
     3544 3551,
     3570 3571,
     3902 3903,
     4139 4140,
     4155 4156,
     4182 4183,
     4194 4196,
     4199 4205,
     4227 4228,
     4231 4236,
     4250 4252,
     6078 6085,
     6087 6088,
     6435 6438,
     6441 6443,
     6448 6449,
     6451 6456,
     6576 6592,
     6600 6601,
     6681 6683,
     6755 6756,
     6765 6770,
     6973 6977,
     6979 6980,
     7078 7079,
     7084 7085,
     7146 7148,
     7154 7155,
     7204 7211,
     7220 7221,
     7410 7411,
     12334 12335,
     43043 43044,
     43136 43137,
     43188 43203,
     43346 43347,
     43444 43445,
     43450 43451,
     43453 43456,
     43567 43568,
     43571 43572,
     43758 43759,
     44003 44004,
     44006 44007,
     44009 44010}
    #{2307 2363 2519 2563 2691 2761 2878 2880 2903 3031 3262 3415 3967
      4145 4152 4239 6070 6741 6743 6753 6916 6965 6971 7042 7073 7082
      7143 7150 7393 43047 43395 43597 43643 43755 43765 44012}),
  "Me"
  (net.cgrand.regex.charset/+
    {1160 1161, 8413 8416, 8418 8420, 42608 42610}),
  "Mn"
  (net.cgrand.regex.charset/+
    {768 879,
     1155 1159,
     1425 1469,
     1473 1474,
     1476 1477,
     1552 1562,
     1611 1631,
     1750 1756,
     1759 1764,
     1767 1768,
     1770 1773,
     1840 1866,
     1958 1968,
     2027 2035,
     2070 2073,
     2075 2083,
     2085 2087,
     2089 2093,
     2137 2139,
     2276 2302,
     2304 2306,
     2369 2376,
     2385 2391,
     2402 2403,
     2497 2500,
     2530 2531,
     2561 2562,
     2625 2626,
     2631 2632,
     2635 2637,
     2672 2673,
     2689 2690,
     2753 2757,
     2759 2760,
     2786 2787,
     2881 2884,
     2914 2915,
     3134 3136,
     3142 3144,
     3146 3149,
     3157 3158,
     3170 3171,
     3276 3277,
     3298 3299,
     3393 3396,
     3426 3427,
     3538 3540,
     3636 3642,
     3655 3662,
     3764 3769,
     3771 3772,
     3784 3789,
     3864 3865,
     3953 3966,
     3968 3972,
     3974 3975,
     3981 3991,
     3993 4028,
     4141 4144,
     4146 4151,
     4153 4154,
     4157 4158,
     4184 4185,
     4190 4192,
     4209 4212,
     4229 4230,
     4957 4959,
     5906 5908,
     5938 5940,
     5970 5971,
     6002 6003,
     6068 6069,
     6071 6077,
     6089 6099,
     6155 6157,
     6432 6434,
     6439 6440,
     6457 6459,
     6679 6680,
     6744 6750,
     6757 6764,
     6771 6780,
     6912 6915,
     6966 6970,
     7019 7027,
     7040 7041,
     7074 7077,
     7080 7081,
     7144 7145,
     7151 7153,
     7212 7219,
     7222 7223,
     7376 7378,
     7380 7392,
     7394 7400,
     7616 7654,
     7676 7679,
     8400 8412,
     8421 8432,
     11503 11505,
     11744 11775,
     12330 12333,
     12441 12442,
     42612 42621,
     42736 42737,
     43045 43046,
     43232 43249,
     43302 43309,
     43335 43345,
     43392 43394,
     43446 43449,
     43561 43566,
     43569 43570,
     43573 43574,
     43698 43700,
     43703 43704,
     43710 43711,
     43756 43757,
     65024 65039,
     65056 65062}
    #{1471 1479 1648 1809 2362 2364 2381 2433 2492 2509 2620 2641 2677
      2748 2765 2817 2876 2879 2893 2902 2946 3008 3021 3260 3263 3270
      3405 3530 3542 3633 3761 3893 3895 3897 4038 4226 4237 4253 6086
      6109 6313 6450 6742 6752 6754 6783 6964 6972 6978 7083 7142 7149
      7405 7412 8417 11647 42607 42655 43010 43014 43019 43204 43443
      43452 43587 43596 43696 43713 43766 44005 44008 44013 64286}),
  "Nd"
  (net.cgrand.regex.charset/+
    {48 57,
     1632 1641,
     1776 1785,
     1984 1993,
     2406 2415,
     2534 2543,
     2662 2671,
     2790 2799,
     2918 2927,
     3046 3055,
     3174 3183,
     3302 3311,
     3430 3439,
     3664 3673,
     3792 3801,
     3872 3881,
     4160 4169,
     4240 4249,
     6112 6121,
     6160 6169,
     6470 6479,
     6608 6617,
     6784 6793,
     6800 6809,
     6992 7001,
     7088 7097,
     7232 7241,
     7248 7257,
     42528 42537,
     43216 43225,
     43264 43273,
     43472 43481,
     43600 43609,
     44016 44025,
     65296 65305}),
  "Nl"
  (net.cgrand.regex.charset/+
    {5870 5872,
     8544 8578,
     8581 8584,
     12321 12329,
     12344 12346,
     42726 42735}
    #{12295}),
  "No"
  (net.cgrand.regex.charset/+
    {178 179,
     188 190,
     2548 2553,
     2930 2935,
     3056 3058,
     3192 3198,
     3440 3445,
     3882 3891,
     4969 4988,
     6128 6137,
     8308 8313,
     8320 8329,
     8528 8543,
     9312 9371,
     9450 9471,
     10102 10131,
     12690 12693,
     12832 12841,
     12872 12879,
     12881 12895,
     12928 12937,
     12977 12991,
     43056 43061}
    #{185 6618 8304 8585 11517}),
  "Pc"
  (net.cgrand.regex.charset/+
    {8255 8256, 65075 65076, 65101 65103}
    #{95 8276 65343}),
  "Pd"
  (net.cgrand.regex.charset/+
    {8208 8213, 11834 11835, 65073 65074}
    #{45 1418 1470 5120 6150 11799 11802 12316 12336 12448 65112 65123
      65293}),
  "Pe"
  (net.cgrand.regex.charset/+
    {12318 12319}
    #{41 93 125 3899 3901 5788 8262 8318 8334 9002 10089 10091 10093
      10095 10097 10099 10101 10182 10215 10217 10219 10221 10223 10628
      10630 10632 10634 10636 10638 10640 10642 10644 10646 10648 10713
      10715 10749 11811 11813 11815 11817 12297 12299 12301 12303 12305
      12309 12311 12313 12315 64831 65048 65078 65080 65082 65084 65086
      65088 65090 65092 65096 65114 65116 65118 65289 65341 65373 65376
      65379}),
  "Pf"
  (net.cgrand.regex.charset/+
    #{187 8217 8221 8250 11779 11781 11786 11789 11805 11809}),
  "Pi"
  (net.cgrand.regex.charset/+
    {8219 8220}
    #{171 8216 8223 8249 11778 11780 11785 11788 11804 11808}),
  "Po"
  (net.cgrand.regex.charset/+
    {33 35,
     37 39,
     46 47,
     58 59,
     63 64,
     182 183,
     1370 1375,
     1523 1524,
     1545 1546,
     1548 1549,
     1566 1567,
     1642 1645,
     1792 1805,
     2039 2041,
     2096 2110,
     2404 2405,
     3674 3675,
     3844 3858,
     4048 4052,
     4057 4058,
     4170 4175,
     4960 4968,
     5741 5742,
     5867 5869,
     5941 5942,
     6100 6102,
     6104 6106,
     6144 6149,
     6151 6154,
     6468 6469,
     6686 6687,
     6816 6822,
     6824 6829,
     7002 7008,
     7164 7167,
     7227 7231,
     7294 7295,
     7360 7367,
     8214 8215,
     8224 8231,
     8240 8248,
     8251 8254,
     8257 8259,
     8263 8273,
     8277 8286,
     11513 11516,
     11518 11519,
     11776 11777,
     11782 11784,
     11790 11798,
     11800 11801,
     11806 11807,
     11818 11822,
     11824 11833,
     12289 12291,
     42238 42239,
     42509 42511,
     42738 42743,
     43124 43127,
     43214 43215,
     43256 43258,
     43310 43311,
     43457 43469,
     43486 43487,
     43612 43615,
     43742 43743,
     43760 43761,
     65040 65046,
     65093 65094,
     65097 65100,
     65104 65106,
     65108 65111,
     65119 65121,
     65130 65131,
     65281 65283,
     65285 65287,
     65294 65295,
     65306 65307,
     65311 65312,
     65380 65381}
    #{42 44 92 161 167 191 894 903 1417 1472 1475 1478 1563 1748 2142
      2416 2800 3572 3663 3860 3973 4347 7379 8275 11632 11787 11803
      12349 12539 42611 42622 43359 44011 65049 65072 65128 65290 65292
      65340 65377}),
  "Ps"
  (net.cgrand.regex.charset/+
    #{40 91 123 3898 3900 5787 8218 8222 8261 8317 8333 9001 10088
      10090 10092 10094 10096 10098 10100 10181 10214 10216 10218 10220
      10222 10627 10629 10631 10633 10635 10637 10639 10641 10643 10645
      10647 10712 10714 10748 11810 11812 11814 11816 12296 12298 12300
      12302 12304 12308 12310 12312 12314 12317 64830 65047 65077 65079
      65081 65083 65085 65087 65089 65091 65095 65113 65115 65117 65288
      65339 65371 65375 65378}),
  "Sc"
  (net.cgrand.regex.charset/+
    {162 165, 2546 2547, 8352 8377, 65504 65505, 65509 65510}
    #{36 1423 1547 2555 2801 3065 3647 6107 43064 65020 65129 65284}),
  "Sk"
  (net.cgrand.regex.charset/+
    {706 709,
     722 735,
     741 747,
     751 767,
     900 901,
     8127 8129,
     8141 8143,
     8157 8159,
     8173 8175,
     8189 8190,
     12443 12444,
     42752 42774,
     42784 42785,
     42889 42890,
     64434 64449}
    #{94 96 168 175 180 184 749 885 8125 65342 65344 65507}),
  "Sm"
  (net.cgrand.regex.charset/+
    {60 62,
     1542 1544,
     8314 8316,
     8330 8332,
     8512 8516,
     8592 8596,
     8602 8603,
     8654 8655,
     8692 8959,
     8968 8971,
     8992 8993,
     9115 9139,
     9180 9185,
     9720 9727,
     10176 10180,
     10183 10213,
     10224 10239,
     10496 10626,
     10649 10711,
     10716 10747,
     10750 11007,
     11056 11076,
     11079 11084,
     65124 65126,
     65308 65310,
     65513 65516}
    #{43 124 126 172 177 215 247 1014 8260 8274 8472 8523 8608 8611
      8614 8622 8658 8660 9084 9655 9665 9839 64297 65122 65291 65372
      65374 65506}),
  "So"
  (net.cgrand.regex.charset/+
    {1550 1551,
     1789 1790,
     3059 3064,
     3841 3843,
     3861 3863,
     3866 3871,
     4030 4037,
     4039 4044,
     4046 4047,
     4053 4056,
     4254 4255,
     5008 5017,
     6622 6655,
     7009 7018,
     7028 7036,
     8448 8449,
     8451 8454,
     8456 8457,
     8470 8471,
     8478 8483,
     8506 8507,
     8524 8525,
     8597 8601,
     8604 8607,
     8609 8610,
     8612 8613,
     8615 8621,
     8623 8653,
     8656 8657,
     8661 8691,
     8960 8967,
     8972 8991,
     8994 9000,
     9003 9083,
     9085 9114,
     9140 9179,
     9186 9203,
     9216 9254,
     9280 9290,
     9372 9449,
     9472 9654,
     9656 9664,
     9666 9719,
     9728 9838,
     9840 9983,
     9985 10087,
     10132 10175,
     10240 10495,
     11008 11055,
     11077 11078,
     11088 11097,
     11493 11498,
     11904 11929,
     11931 12019,
     12032 12245,
     12272 12283,
     12306 12307,
     12342 12343,
     12350 12351,
     12688 12689,
     12694 12703,
     12736 12771,
     12800 12830,
     12842 12871,
     12896 12927,
     12938 12976,
     12992 13054,
     13056 13311,
     19904 19967,
     42128 42182,
     43048 43051,
     43062 43063,
     43639 43641,
     65517 65518,
     65532 65533}
    #{166 169 174 176 1154 1758 1769 2038 2554 2928 3066 3199 3449 3859
      3892 3894 3896 6464 8468 8485 8487 8489 8494 8522 8527 8659 12292
      12320 12880 43065 65021 65508 65512}),
  "Zl" (net.cgrand.regex.charset/+ #{8232}),
  "Zp" (net.cgrand.regex.charset/+ #{8233}),
  "Zs"
  (net.cgrand.regex.charset/+
    {8192 8202}
    #{32 160 5760 6158 8239 8287 12288})})