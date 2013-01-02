<?php

  if ($argc != 5) {
      echo "Syntax: <method> <url> <seconds> <key>";
      echo "Example: GET https://storage101.dfw1.clouddrive.com/v1/" .
           "MossoCloudFS_12345678-9abc-def0-1234-56789abcdef0/" .
           "container/my_cat.jpg 60 my_shared_secret_key";
  } else {
    $method = $argv[1];
    $url = $argv[2];
    $seconds = $argv[3];
    $key = $argv[4];
    $method = strtoupper($method);
    list($base_url, $object_path) =  split("/v1/", $url);
    $object_path = "/v1/$object_path";
    $seconds = (int)$seconds;
    $expires = (int)(time() + $seconds);
    $hmac_body = "$method\n$expires\n$object_path";
    
    $sig = hash_hmac("sha1", $hmac_body, $key);
    $temp_url = "$base_url$object_path?temp_url_sig=$sig&temp_url_expires=$expires";
    
    echo "Temp URL: $temp_url";
    echo "Here is the curl command to upload a yak via PUT ...\n";
    echo "curl -v -X PUT -T ../src/main/resources/Bos_grunniens_at_Letdar_on_Annapurna_Circuit.jpg \"$temp_url\"\n";
  }
