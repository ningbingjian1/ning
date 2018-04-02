package main

import (
	"fmt"
	"time"
)

/**
时间模块 time
https://studygolang.com/articles/8668
 */
import (
	"time"
	"fmt"
)

func main() {
	//当前时间戳
	fmt.Println(time.Now().Unix())

	//当前格式化时间
	fmt.Println(time.Now().Format("2006-01-02 15:04:05"))
	// 这是个奇葩,必须是这个时间点, 据说是go诞生之日, 记忆方法:6-1-2-3-4-5
	fmt.Println(time.Now().Format("2006-01-02"))



	//时间戳转str格式化时间
	str_time := time.Unix(1389058332, 0).Format("2006-01-02 15:04:05")
	fmt.Println(str_time)
	// 2014-01-07 09:32:12

	//str格式化时间转时间戳
	the_time := time.Date(2014, 1, 7, 5, 50, 4, 0, time.Local)
	unix_time := the_time.Unix()
	fmt.Println(unix_time)
	// 389045004

	the_time, err := time.Parse("2006-01-02 15:04:05", "2014-01-08 09:04:41")
	if err == nil {
		unix_time := the_time.Unix()
		fmt.Println(unix_time)
	}
	// 1389171881




	t, _ := time.Parse("2006-01-02 15:04:05", "2016-04-20 16:23:00")
	fmt.Println(t.Unix())
	y, m, d := time.Unix(1466344320, 0).Date()
	fmt.Println(y, m, d)

	//format后面的字符串必须是2006-01-02 15:04:05，据说go是这个时间诞生的
	fmt.Println(time.Now().Format("2006-01-02 15:04:05"))
	fmt.Println(time.Now().Format("2006-01-02"))
	fmt.Println(time.Now().Format("20060102"))

	select {
	case <-time.After(5 * time.Second):
		fmt.Println("After 5 second")
	}
	c := time.Tick(10 * time.Second)
	for now := range c {
		fmt.Println(now)
	}

}