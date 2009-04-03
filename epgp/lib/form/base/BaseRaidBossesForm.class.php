<?php

/**
 * RaidBosses form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseRaidBossesForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'      => new sfWidgetFormInputHidden(),
      'name'    => new sfWidgetFormInput(),
      'zone'    => new sfWidgetFormPropelChoice(array('model' => 'RaidZones', 'add_empty' => false)),
      'baseval' => new sfWidgetFormInput(),
    ));

    $this->setValidators(array(
      'id'      => new sfValidatorPropelChoice(array('model' => 'RaidBosses', 'column' => 'id', 'required' => false)),
      'name'    => new sfValidatorString(array('max_length' => 255)),
      'zone'    => new sfValidatorPropelChoice(array('model' => 'RaidZones', 'column' => 'id')),
      'baseval' => new sfValidatorNumber(),
    ));

    $this->validatorSchema->setPostValidator(
      new sfValidatorPropelUnique(array('model' => 'RaidBosses', 'column' => array('name')))
    );

    $this->widgetSchema->setNameFormat('raid_bosses[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'RaidBosses';
  }


}
