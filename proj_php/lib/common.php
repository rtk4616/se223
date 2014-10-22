<?php
function ShowErr($msg)
{
  if(defined('SAE_MYSQL_DB'))
  {
    sae_set_display_errors(false);
    sae_debug($msg);
    sae_set_display_errors(true);
  }
  else 
    error_log($msg . "\n", 3, "./log.txt");
}

function SetUserInfo($uid, $un, $pwmd5)
{
  //$_SESSION["id"] = $uid;
  //$_SESSION["un"] = $un;
  $token = $uid . "|" . $un . "|" . $pwmd5;
  $token = authcode($token, 'ENCODE');
  setcookie("token", $token, time() + 3600 * 24 * 30);
}

function GetUserInfo()
{
  /*if($_SESSION['un'] != "" && 
     $_SESSION['id'] != "")
	return array('id' => $_SESSION['id'],
	             'un' => $_SESSION['un']);*/

  $token = $_COOKIE["token"];
  if($token == "") return false;
  $token = authcode($token, 'DECODE');
  if($token == "") return false;
  $arr = explode("|", $token);
  //0: uid, 1: un, 2: md5(pw)
  
  include "./config.php";
  $conn = mysql_connect($db_host, $db_user, $db_pwd);
  if(!$conn) return false;
  if(!mysql_select_db($db_name, $conn))
    return false;
	
  $sql = "SELECT * FROM users WHERE u_id=$arr[0] AND u_un='$arr[1]' AND u_pw='$arr[2]'";
  $res = mysql_query($sql, $conn);
  if(!$res) return false;
  if(mysql_num_rows($res) == 0) return false;

  $arr = mysql_fetch_array($res);
  $r = array('id' => $arr[0], 'un' => $arr[1]);
  //$_SESSION["un"] = $arr[1];
  //$_SESSION["id"] = $arr[0];
  mysql_close($conn);
  return $r;
}

function showmsg($msg, $from)
{ ?>
<html>
  <head>
    <title>错误信息</title>
    <meta charset="utf-8" />
  </head>
  <body>
    <p>
	  <?php echo $msg; ?>
	  <a href="<?php echo $from; ?>">返回</a>
	</p>
  </body>
</html>
<?php
}

/** 
 * @param string $string 原文或者密文 
 * @param string $operation 操作(ENCODE | DECODE), 默认为 DECODE 
 * @param string $key 密钥 
 * @param int $expiry 密文有效期, 加密时候有效， 单位 秒，0 为永久有效 
 * @return string 处理后的 原文或者 经过 base64_encode 处理后的密文 
 * 
 * @example 
 * 
 * $a = authcode('abc', 'ENCODE', 'key'); 
 * $b = authcode($a, 'DECODE', 'key');  // $b(abc) 
 * 
 * $a = authcode('abc', 'ENCODE', 'key', 3600); 
 * $b = authcode('abc', 'DECODE', 'key'); // 在一个小时内，$b(abc)，否则 $b 为空 
 */  
function authcode($string, $operation = 'DECODE', $key = '', $expiry = 0) 
{  
      
    $ckey_length = 4;  
    // 随机密钥长度 取值 0-32;  
    // 加入随机密钥，可以令密文无任何规律，即便是原文和密钥完全相同，加密结果也会每次不同，增大破解难度。  
    // 取值越大，密文变动规律越大，密文变化 = 16 的 $ckey_length 次方  
    // 当此值为 0 时，则不产生随机密钥  
      
  
    $key = md5 ( $key ? $key : 'key' ); //这里可以填写默认key值  
    $keya = md5 ( substr ( $key, 0, 16 ) );  
    $keyb = md5 ( substr ( $key, 16, 16 ) );  
    $keyc = $ckey_length ? ($operation == 'DECODE' ? substr ( $string, 0, $ckey_length ) : substr ( md5 ( microtime () ), - $ckey_length )) : '';  
      
    $cryptkey = $keya . md5 ( $keya . $keyc );  
    $key_length = strlen ( $cryptkey );  
      
    $string = $operation == 'DECODE' ? base64_decode ( substr ( $string, $ckey_length ) ) : sprintf ( '%010d', $expiry ? $expiry + time () : 0 ) . substr ( md5 ( $string . $keyb ), 0, 16 ) . $string;  
    $string_length = strlen ( $string );  
      
    $result = '';  
    $box = range ( 0, 255 );  
      
    $rndkey = array ();  
    for($i = 0; $i <= 255; $i ++) {  
        $rndkey [$i] = ord ( $cryptkey [$i % $key_length] );  
    }  
      
    for($j = $i = 0; $i < 256; $i ++) {  
        $j = ($j + $box [$i] + $rndkey [$i]) % 256;  
        $tmp = $box [$i];  
        $box [$i] = $box [$j];  
        $box [$j] = $tmp;  
    }  
      
    for($a = $j = $i = 0; $i < $string_length; $i ++) {  
        $a = ($a + 1) % 256;  
        $j = ($j + $box [$a]) % 256;  
        $tmp = $box [$a];  
        $box [$a] = $box [$j];  
        $box [$j] = $tmp;  
        $result .= chr ( ord ( $string [$i] ) ^ ($box [($box [$a] + $box [$j]) % 256]) );  
    }  
      
    if ($operation == 'DECODE') {  
        if ((substr ( $result, 0, 10 ) == 0 || substr ( $result, 0, 10 ) - time () > 0) && substr ( $result, 10, 16 ) == substr ( md5 ( substr ( $result, 26 ) . $keyb ), 0, 16 )) {  
            return substr ( $result, 26 );  
        } else {  
            return '';  
        }  
    } else {  
        return $keyc . str_replace ( '=', '', base64_encode ( $result ) );  
    }  
  
}
?>