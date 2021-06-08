package com.example.klp.datas

//말머리, 글제목, 글쓴이 아이디, 성취도 점수, 좋아요 개수, 댓글 개수
data class Article(var category:String, var Title:String, var author:String, var goalScore:Int, var prefNum:Int, var commentNum:Int)
