<?php

class Zones extends BaseZones
{
  public function getRoute(){
    return 'zone/show?name='.$this->name;
  }
  
  public function __toString(){
    return $this->name;
  }
  
  public function echoLink(){
    echo link_to($this, $this->getRoute());
  }
}
