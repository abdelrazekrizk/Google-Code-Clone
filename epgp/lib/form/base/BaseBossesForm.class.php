<?php

/**
 * Bosses form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseBossesForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'      => new sfWidgetFormInputHidden(),
      'name'    => new sfWidgetFormInput(),
      'zone'    => new sfWidgetFormPropelChoice(array('model' => 'Zones', 'add_empty' => false)),
      'baseval' => new sfWidgetFormInput(),
    ));

    $this->setValidators(array(
      'id'      => new sfValidatorPropelChoice(array('model' => 'Bosses', 'column' => 'id', 'required' => false)),
      'name'    => new sfValidatorString(array('max_length' => 255)),
      'zone'    => new sfValidatorPropelChoice(array('model' => 'Zones', 'column' => 'id')),
      'baseval' => new sfValidatorNumber(),
    ));

    $this->validatorSchema->setPostValidator(
      new sfValidatorPropelUnique(array('model' => 'Bosses', 'column' => array('name')))
    );

    $this->widgetSchema->setNameFormat('bosses[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'Bosses';
  }


}
