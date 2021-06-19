package com.example.klp.data

import java.io.Serializable

//말머리, 글제목, 글쓴이 아이디, 성취도 점수, 좋아요 개수, 댓글 개수
data class Article(var UID:Int, var FID:Int, var FORM_HEAD:String, var BODY:String, var FORM_LIKE:Int):Serializable
