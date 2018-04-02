package loop

import "fmt"

/**
计算0到n两两配对相加 n = 3 输出
0 + 3 = 3
1 + 2 = 3
3 + 0 = 3
 */
func L1(n int )  {
	for i := 0 ; i < n ; i ++  {
		fmt.Printf("%d + %d = %d \n", i , n -i , n )
	}
}
